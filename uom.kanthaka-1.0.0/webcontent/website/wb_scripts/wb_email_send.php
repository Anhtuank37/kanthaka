<?php
	// check to ensure that all parameters are ok as well as protect from bots
	// and hackers
	if (!isset($_POST['wb_form_id'])
		|| $_POST['message'] !== ''
		|| !isset($forms)
		|| !is_array($forms)
		|| !isset($page_id)
		|| !(isset($forms[$page_id]) || isset($forms['blog']))
		|| !(isset($forms[$page_id][$_POST['wb_form_id']]) || isset($forms['blog'][$_POST['wb_form_id']]))
		|| !(isset($forms[$page_id][$_POST['wb_form_id']]['fields']) || isset($forms['blog'][$_POST['wb_form_id']]['fields']))
		|| isset($_POST['forms'])
		|| isset($_GET['forms'])
	) return;

	include dirname(__FILE__).'/phpmailer/class.phpmailer.php';
	$form = isset($forms[$page_id][$_POST['wb_form_id']])
		? $forms[$page_id][$_POST['wb_form_id']] : $forms['blog'][$_POST['wb_form_id']];
	$fields = $form['fields'];
	$mail_to = is_mail($form['email']);
	$mail_from = $mail_to;
	$mail_from_name = 'NoName';
	
	$data = Array();
	foreach($fields as $idx => $field) {
		if (!isset($_POST["wb_input_$idx"])) return; // all fields must be present
		$max_len = ($field["type"]=="textarea")?65536:1024; // 65 kilobytes max for textarea and 1024 for other
		$value = $_POST["wb_input_$idx"];
		$value = htmlspecialchars($value);
		$value = @substr($value, 0, $max_len);
		if ($field["type"] == "select") {
			$options = explode(";", $field["options"]);
			$data[$idx] = trim($options[intval($value)]);
		} else
			$data[$idx] = $value;
		if ($field["fidx"] == 0) $mail_from_name = $value;
		if ($field["fidx"] == 1) $mail_from = is_mail($value);
	}

	if (!$mail_from_name) $mail_from_name = "Anonymous";
	if (!$mail_from) $mail_from = $mail_to;
	
	global $wb_form_send_state;
	$wb_form_send_state = false;
	
	if ($mail_to) {
		$mailer = new PHPMailer();
		$mailer->PluginDir = dirname(__FILE__) . "/phpmailer/";

		$style = "* { font: 12px Arial; }\nstrong { font-weight: bold; }";
		$mailer->AddAddress($mail_to);
		$mailer->SetFrom($mail_from, $mail_from_name);
		$mailer->CharSet = 'utf-8';
		//$mailer->MsgHTML(preg_replace('/([\x{80}-\x{FFFFFF}])/ue', "mb_convert_encoding('$1', 'HTML-ENTITIES', 'UTF-8')", $tpl->getHTML()));
		$message = '<table cellspacing="5" cellpadding="0">';
		foreach ($fields as $idx => $field) {
			$name = $field["name"];
			$value = $data[$idx];
			if ($field["type"] == "textarea")
				$message .= "<tr><td colspan=\"2\"><strong>$name: </strong></td></tr>\n<tr><td colspan=\"2\">" . nl2br($value) . "</td></tr>\n";
			else
				$message .= "<tr><td><strong>$name: </strong></td><td>" . nl2br($value) . "</td></tr>\n";
  		}
		$message .= '</table>';

		$html =
'<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
	<head>
		<title>' . $form["subject"] . '</title>
		<meta http-equiv=Content-Type content="text/html; charset=utf-8">
		' . ($style?"<style><!--\n$style\n--></style>\n\t\t":"") . '</head>
	<body>' . $message . '</body>
</html>';
		$mailer->MsgHTML($html);
		$mailer->AltBody = strip_tags(str_replace("</tr>", "</tr>\n", $message));
		$mailer->Subject = $form["subject"];
		$mailer->Send();
		$wb_form_send_state = 'Form was sent.';
	}
?>