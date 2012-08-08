<?php
include("config.php");
session_start();

if($_SERVER["REQUEST_METHOD"] == "POST")
{
	$myusername=addslashes($_POST['username']); 
	$mypassword=addslashes($_POST['password']); 

	$sql="SELECT id FROM users WHERE username='$myusername' and passcode='$mypassword'";
	$result=mysql_query($sql);
	$row=mysql_fetch_array($result);
	$active=$row['active'];

	$count=mysql_num_rows($result);

	if($count==1)
	{
		session_register("myusername");
		$_SESSION['login_user']=$myusername;
		header("location: viewpromotions.php");
	}
	else 
	{
		$error="Your Username or Password is invalid!";
	}
}
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Kanthaka Dashboard</title>
	<meta http-equiv="content-language" content="en" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="shortcut icon" href="images/favicon.png" type="image/x-icon"/>
	<link rel="stylesheet" href="css/styles.css" type="text/css" media="screen" />
	<!--[if lte IE 7]>
	<link href="css/styles_ie.css" rel="stylesheet" type="text/css" media="screen" />
	<![endif]-->
	<!--[if lte IE 6]>
	<link href="css/styles_ie6.css" rel="stylesheet" type="text/css" media="screen" />
	<![endif]-->
	<script type="text/javascript" src="js/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="js/ui.js"></script>
	
</head>

<body>

<div id="wrap">

	<!--Hearder-->
	<div id="header">
		<h1><a class="bg" href="#" title="Home"></a>Kanthaka Dashboard</h1>
		<h2><span></span></h2>
		<p id="description"></p>
	</div>
	
	<!--Side bar-->
    <div id="sidebar">
		<ul class="items">
            
		</ul>
		<br><br><br><br><br><br><br><br><br><br><br><br><br>
		<span class="clear"></span>
		<div id="redbox">
			<h4>Kanthaka</h4>
			<p>
				<big>Copyright 2012</big>
				University of Moratuwa, Sri Lanka<br />
				Version: 1.0.0
			</p>
		</div>
	</div>
	
	<!--Content-->
	<div id="content">
		<h2>Login</h2>
				
			<form method="post" action="">
			<fieldset>
				<div id="basic">
					<label>Username:</label>
					<span class="small_input"><input class="small" name="username" type="text" value="" /></span><br class="hid" />
					<label>Password:</label>
					<span class="small_input"><input class="small" name="password" type="password" value="" /></span><br class="hid" />
				</div>
				<br><br><br><br class="hid" /><br><br>
				<a href="#" class="submit button" title="Login"><span>Login</span></a>
				<span class="clear"></span><br><br>
			</fieldset>
			</form>
			<?php
				if ($error!=''){
				echo '<p class="alert">';
				echo '	<span class="txt"><span class="icon"></span><strong>Alert: </strong>' . $error . '</span>';
				echo '	<a href="#" class="close" title="Close"><span class="bg"></span>Close</a>';
				echo '</p>';
				}
			?>
	</div>
	<div id="footer" style="height:140px;">
	</div>
</div>

</body>

</html>