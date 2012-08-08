<?php
include('lock.php');
include('config.php');

$username = $_POST["username"]; 
$password = $_POST["password"]; 
$firstname = $_POST["firstname"]; 
$lastname = $_POST["lastname"]; 
$email = $_POST["email"]; 
$slevel = $_POST["slevel"]; 

if ($username!=""){
	$res = mysql_query("INSERT INTO users (username, passcode, first_name, last_name, email, sec_level)
	VALUES ('$username', '$password', '$firstname', '$lastname', '$email', '$slevel');");
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
		<p id="status">Logged in as <?php echo $login_session; ?>, <a href="logout.php" title="Sign Out?">Sign Out?</a></p>
		<h2><span>View Promotions</span></h2>
		<p id="description">Current users of the Admin portal.</p>
	</div>
	
	<!--Side bar-->
    <div id="sidebar">
		<ul class="items">
            <li class="subitems">
            	<a href="#" title="Promotions">Promotions</a>
				<ul>
                    <li><a href="addpromotion.php" title="Add Promotion">Add New Promotion</a></li>
                    <li><a href="viewpromotions.php" title="View Promotions">View Promotions</a></li>
				</ul>
            </li>
			<li class="active">
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
		<h2>Promotion List</h2>
		
		<div class="box">
			<table>
				<tr>
					<th class="col1">Userame</th>
					<th class="col2">Fisrt Name</th>
					<th class="col3">Last Name</th>
					<th class="col4">Email</th>
					<th class="col4">Security Level</th>
					<th class="col6"></th>
				</tr>
				
				<?php
				
					$result = mysql_query("SELECT * FROM users");
					
					$i=1;
					while($row = mysql_fetch_array($result))
					{
						echo '<tr '; if($i%2==0){ echo 'class="highlight"';} echo ' >';
						echo '	<td><a href="#" title="'.$row['username'].'">'.$row['username'].'</a></td>';
						echo '	<td>' . $row['first_name'] . '</td>';
						echo '	<td>' . $row['last_name'] . '</td>';
						echo '	<td>' . $row['email'] . '</td>';
						echo '	<td>' . $row['sec_level'] . '</td>';
						echo '	<td class="action">
									<ul>
										<li><a class="edit" href="#" title="edit"></a></li>
										<li><a class="delete" href="#" title="delete"></a></li>
									</ul>
								</td>';
						echo '</tr>';
						$i++;
					}
					
				?>
			</table>
		</div>
		<br class="hid" /><br><br>
		<a href="adduser.php" class="button" title="Add User"><span>Add User</span></a>
		<span class="clear"></span><br><br>
		<?php
		if ($res==1){
			echo '<p id="congrats" class="alert">
			<span class="txt"><span class="icon"></span><strong>Congrats:</strong> The user you added successfully saved!</span>
			<a href="#" class="close" title="Close"><span class="bg"></span>Close</a>
			</p>';
		}
		?>
		
	</div>
	<div id="footer">
	</div>
</div>

</body>

</html>
