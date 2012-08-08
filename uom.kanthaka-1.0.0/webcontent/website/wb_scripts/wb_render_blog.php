<?php
error_reporting(E_ALL);
ini_set('display_errors', 'On');

$bpl = $base_dir.'/blog.dat';
$cmm = $base_dir.'/dat/blog.'.$postId.'.comments.dat';
$old = umask(0);
@chmod($base_dir.'/dat', 0777);
umask($old);

if (isset($_POST['add_comment']) && $_POST['add_comment']) {
	if (is_file($cmm)) {
		$comments = @file_get_contents($cmm);
		$comments = @unserialize($comments);
		if (!is_array($comments))
			$comments = Array();
		
		$newId = (count($keys = array_keys($comments)) ? intval(max($keys)) : 0) + 1;
		$txt = isset($_POST['text']) ? trim($_POST['text']) : '';
		if ($txt) {
			$comments[$newId] = Array(
				'user' => isset($_POST['user']) ? $_POST['user'] : 'no-name',
				'date' => date('Y-m-d'),
				'time' => date('H:i'),
				'text' => htmlspecialchars($txt)
			);
			$old = umask(0);
			@chmod($cmm, 0777);
			umask($old);
			@file_put_contents($cmm, serialize($comments));
			$out = post_comment($postId, $comments[$newId]['user'], $comments[$newId]['text']);
			//wf_log(print_r($out, true));
		}
	}
	header('Location: ./?view=blog&postId='.$postId); exit();
}

if ($postId !== false) {
	$post = $base_dir.'/blog.'.$postId.'.php';
	if (is_file($post)) {
		$post = file_get_contents($post);
		ob_start();
		eval("\n ?".">".$post."<"."?php \n");
		$post = ob_get_clean();
		$post = preg_replace('#<!-- .* -->#ismU', '', $post);
		echo $post;
	}
	if (is_file($cmm)) {
		$comments = @file_get_contents($cmm);
		$comments = @unserialize($comments);
		if (is_array($comments)) {
?>
<div class="wb_blog-comment-list">
	<span class="wb_blog-comment-head">Comments</span>
	<ul>
	<?php foreach ($comments as $iid => $li) { ?>
		<li>
			<div class="wb_blog-comment-title">
				<?php echo $li['user']; ?>
				<span class="wb_blog-comment-date"><?php echo $li['date'].' '.$li['time']; ?></span>
			</div>
			<div class="wb_blog-comment-text"><?php echo $li['text']; ?></div>
		</li>
	<?php } ?>
	</ul>
	<form id="comment_form" action="" method="post">
		<input type="hidden" name="add_comment" value="1" />
		<input type="hidden" name="view" value="blog" />
		<input type="hidden" name="postId" value="<?php echo htmlspecialchars($postId); ?>" />
		<table>
			<tr>
				<td>Name:&nbsp;</td>
				<td><input type="text" name="user" value="" /></td>
			</tr>
			<tr>
				<td>Comment:&nbsp;</td>
				<td><textarea name="text" rows="4" cols="40"></textarea></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<a class="wb_btn" href="javascript:void(0)" onclick="document.getElementById('comment_form').submit();" title="Post"><span>Post</span></a>
				</td>
			</tr>
		</table>
	</form>
</div>
<?php
		}
	}
} else if ($blogId !== false) {
	$blogs = @file_get_contents($bpl);
	$blogs = @unserialize($blogs);
	if (!is_array($blogs))
		$blogs = Array();
	if (isset($blogs[$blogId]['list']) && is_array($blogs[$blogId]['list'])) {
?>
<div>
	<ul class="wb_blog-list">
	<?php foreach ($blogs[$blogId]['list'] as $pid) { ?>
		<?php
			$post = $base_dir.'/blog.'.$pid.'.php';
			if (!is_file($post))
				continue;
			$post = file_get_contents($post);
			ob_start();
			eval("\n ?".">".$post."<"."?php \n");
			$post = ob_get_clean();
			$name = preg_match('#<!-- name -->([^\0]*)<!-- /name -->#ismU', $post, $m)
				? $m[1] : '';
			$date = preg_match('#<!-- date -->([^\0]*)<!-- /date -->#ismU', $post, $m)
				? $m[1] : '';
			$text = preg_match('#<!-- text -->([^\0]*)<!-- /text -->#ismU', $post, $m)
				? $m[1] : '';
		?>
		<li>
			<?php echo $name; ?>
			<?php echo $date; ?>
		<?php if (true) { ?>
			<?php echo $text; ?>
		<?php } else { ?>
			<div class="wb_blog-text"><?php echo mini_text($text); ?></div>
		<?php } ?>
		</li>
	<?php } ?>
	</ul>
</div>
<?php
	}
}


?>