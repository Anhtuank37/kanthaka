<?php
include('lock.php');
include('config.php');
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

<?php 
	$name = $_POST["name"]; 
	$type = $_POST["type"]; 
	$dateStart = $_POST["dateStart"]; 
	$dateEnd = $_POST["dateEnd"]; 
	$description = $_POST["description"]; 
	$r1c1r = $_POST["r1c1r"]; 
	$r1c1o = $_POST["r1c1o"];
	$r1c1v = $_POST["r1c1v"]; 
	$r1c2r = $_POST["r1c2r"]; 
	$r1c2o = $_POST["r1c2o"]; 
	$r1c2v = $_POST["r1c2v"];
	$r2c1r = $_POST["r2c1r"]; 
	$r2c1o = $_POST["r2c1o"]; 
	$r2c1v = $_POST["r2c1v"];
	$r2c2r = $_POST["r2c2r"]; 
	$r2c2o = $_POST["r2c2o"]; 
	$r2c2v = $_POST["r2c2v"];
	$r3c1r = $_POST["r3c1r"]; 
	$r3c1o = $_POST["r3c1o"]; 
	$r3c1v = $_POST["r3c1v"];
	$r3c2r = $_POST["r3c2r"]; 
	$r3c2o = $_POST["r3c2o"]; 
	$r3c2v = $_POST["r3c2v"];
	$r4c1r = $_POST["r4c1r"]; 
	$r4c1o = $_POST["r4c1o"]; 
	$r4c1v = $_POST["r4c1v"]; 
	$r4c2r = $_POST["r4c2r"]; 
	$r4c2o = $_POST["r4c2o"]; 
	$r4c2v = $_POST["r4c2v"];
	$r5c1r = $_POST["r5c1r"]; 
	$r5c1o = $_POST["r5c1o"]; 
	$r5c1v = $_POST["r5c1v"];
	$r5c2r = $_POST["r5c2r"]; 
	$r5c2o = $_POST["r5c2o"]; 
	$r5c2v = $_POST["r5c2v"];
	$r6c1r = $_POST["r6c1r"]; 
	$r6c1o = $_POST["r6c1o"]; 
	$r6c1v = $_POST["r6c1v"];
	$r6c2r = $_POST["r6c2r"]; 
	$r6c2o = $_POST["r6c2o"]; 
	$r6c2v = $_POST["r6c2v"];
	
	$rule = "";
	$rule = $_POST["rule"];
	
	$r = array();
	$o = array();
	$v = array();

	if ($r1c1r!="") {
		$rule.="( ".$r1c1r." ".$r1c1o." ".$r1c1v;
		array_push($r,$r1c1r);
		array_push($o,$r1c1o);
		array_push($v,$r1c1v);
		if ($r1c2r!="") {
			$rule.=" && ".$r1c2r." ".$r1c2o." ".$r1c2v." )"; 
			array_push($r,$r1c2r);
			array_push($o,$r1c2o);
			array_push($v,$r1c2v);
		}
		else $rule.=" )";
	}
	if ($r2c1r!="") {
		$rule.=" || ( ".$r2c1r." ".$r2c1o." ".$r2c1v;
		array_push($r,$r2c1r);
		array_push($o,$r2c1o);
		array_push($v,$r2c1v);
		if ($r2c2r!="") {
			$rule.=" && ".$r2c2r." ".$r2c2o." ".$r2c2v." )";
			array_push($r,$r2c2r);
			array_push($o,$r2c2o);
			array_push($v,$r2c2v);			
		}
		else $rule.=" )";
	}
	if ($r3c1r!="") {
		$rule.=" || ( ".$r3c1r." ".$r3c1o." ".$r3c1v;
		array_push($r,$r3c1r);
		array_push($o,$r3c1o);
		array_push($v,$r3c1v);
		if ($r3c2r!="") {
			$rule.=" && ".$r3c2r." ".$r3c2o." ".$r3c2v." )"; 
			array_push($r,$r3c2r);
			array_push($o,$r3c2o);
			array_push($v,$r3c2v);	
		}
		else $rule.=" )";
	}
	if ($r4c1r!="") {
		$rule.=" || ( ".$r4c1r." ".$r4c1o." ".$r4c1v;
		array_push($r,$r4c1r);
		array_push($o,$r4c1o);
		array_push($v,$r4c1v);
		if ($r4c2r!="") {
			$rule.=" && ".$r4c2r." ".$r4c2o." ".$r4c2v." )"; 
			array_push($r,$r4c2r);
			array_push($o,$r4c2o);
			array_push($v,$r4c2v);
		}
		else $rule.=" )";
	}
	if ($r5c1r!="") {
		$rule.=" || ( ".$r5c1r." ".$r5c1o." ".$r5c1v;
		array_push($r,$r5c1r);
		array_push($o,$r5c1o);
		array_push($v,$r5c1v);
		if ($r5c2r!="") {
			$rule.=" && ".$r5c2r." ".$r5c2o." ".$r5c2v." )";
			array_push($r,$r5c2r);
			array_push($o,$r5c2o);
			array_push($v,$r5c2v);			
		}
		else $rule.=" )";
	}
	if ($r6c1r!="") {
		$rule.=" || ( ".$r6c1r." ".$r6c1o." ".$r6c1v;
		array_push($r,$r6c1r);
		array_push($o,$r6c1o);
		array_push($v,$r6c1v);
		if ($r6c2r!="") {
			$rule.=" && ".$r6c2r." ".$r6c2o." ".$r6c2v." )"; 
			array_push($r,$r6c2r);
			array_push($o,$r6c2o);
			array_push($v,$r6c2v);
		}
		else $rule.=" )"; 
	}
	
?>

<?php

	mysql_query("INSERT INTO promotions2 (name, type, start_date, end_date, description, rule)
	VALUES ('$name', '$type', '$dateStart', '$dateEnd', '$description', '$rule');");
	
	mysql_query("UPDATE promotions SET type='$type', start_date='$dateStart', end_Date='$dateEnd', description='$description'
			WHERE name='$name'");

	//mysql_close($con);
?>

<body>

<div id="wrap">

	<!--Hearder-->
	<div id="header">
		<h1><a class="bg" href="#" title="Home"></a>Kanthaka Dashboard</h1>
		<p id="status">Logged in as <?php echo $login_session; ?>, <a href="logout.php" title="Sign Out?">Sign Out?</a></p>
		<h2><span>New Promotion</span></h2>
		<p id="description">Add a new sales promotion to execute.</p>
	</div>
	
	<!--Side bar-->
    <div id="sidebar">
		<ul class="items">
            <li class="active">
            	<a href="#" title="Promotions">Promotions</a>
				<ul>
                    <li><a href="addpromotion.php" title="Add Promotion">Add New Promotion</a></li>
                    <li><a href="viewpromotions.php" title="View Promotions">View Promotions</a></li>
					<li><a href="viewselections.php" title="View Promotions">View Selections</a></li>
				</ul>
            </li>
			<li class="subitems">
				<a href="#" title="Users">Users</a>
				<ul>
					<li><a href="adduser.php" title="Add User">Add New User</a></li>
					<li><a href="viewusers.php" title="View Users">View Users</a></li>
				</ul>
			</li>
			<li class="subitems">
				<a href="#" title="Settings">Settings</a>
				<ul>
                    <li><a href="#" title="Configurations">Configurations</a></li>
                    <li><a href="#" title="System Settings">System Settings</a></li>
				</ul>
			</li>
			<li class="subitems">
				<a href="#" title="Support">Support</a>
				<ul>
					<li><a href="#" title="FAQ">FAQ</a></li>
					<li><a href="#" title="Get Support">Get Support</a></li>

				</ul>
			</li>
		</ul>
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
		<h2>Promotion Details</h2>
		
		<ul class="tabs">
			<li id="tsuccess" class="active"><a href="" title="Success"><span>Success</span></a></li>
		</ul>
		
		<form method="post" action="">
		<fieldset>
			<div id="basic">
				<table border="0" cellpadding="5" cellspacing="0">
					<tr>
						<td width="120">
							<label>Promotion Name:</label>
						</td>
						<td>
							<?php echo $name; ?>
						</td>
					</tr>
					<tr>
						<td>
							<label>Promotion Type:</label>
						</td>
						<td>
							<?php echo $type; ?>
						</td>
					</tr>
					<tr>
						<td>
							<label>Running Period:</label>
						</td>
						<td>
							<?php echo $dateStart." to ".$dateEnd; ?>
						</td>
					</tr>
					<tr>
						<td>
							<label>Description:</label>
						</td>
						<td>
							<?php echo $description; ?>
						</td>
					</tr>
					<tr>
						<td>
							<label>Promotion Rules:</label>
						</td>
						<td>
							<?php echo $rule; ?>
						</td>
					</tr>
				</table>
				<br class="hid" /><br>
					
			</div>

		</fieldset>
		</form>
		
		<p id="congrats" class="alert">
			<span class="txt"><span class="icon"></span><strong>Congrats!</strong> The New Promotion you added successfully saved.</span>
			<a href="#" class="close" title="Close"><span class="bg"></span>Close</a>
		</p>
		
		<a href="viewpromotions.php" class="button" title="Show All Promotions"><span>Show All Promotions</span></a>
		<span class="clear"></span>

		
	</div>
	<div id="footer">
	</div>
</div>

</body>

</html>
