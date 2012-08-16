<?php

$npl = $base_dir.'/news.dat';

if ($postId !== false) {
	$post = $base_dir.'/news.'.$postId.'.php';
	if (is_file($post)) {
		$post = file_get_contents($post);
		$post = preg_replace('#<!-- .* -->#ismU', '', $post);
		echo $post;
	}
} else if ($feedId !== false) {
	$blogs = @file_get_contents($npl);
	$blogs = @unserialize($blogs);
	if (!is_array($blogs))
		$blogs = Array();
	if (isset($blogs[$feedId]['list']) && is_array($blogs[$feedId]['list'])) {
?>
<div>
	<ul class="wb_blog-list">
	<?php foreach ($blogs[$feedId]['list'] as $pid) { ?>
		<?php
			$post = $base_dir.'/news.'.$pid.'.php';
			if (!is_file($post))
				continue;
			$post = file_get_contents($post);
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