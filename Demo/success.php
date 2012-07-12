<html>
<head>
	<link rel="stylesheet" media="screen" type="text/css" href="css/datepicker.css" />
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/datepicker.js"></script>
    <script type="text/javascript" src="js/eye.js"></script>
    <script type="text/javascript" src="js/utils.js"></script>
    <script type="text/javascript" src="js/layout.js?ver=1.0.2"></script>
	<style type="text/css">
		.or {font-weight:bold; color:black; width:100px;}
		.and {font-weight:bold; color:blue;}
		.rowBorder {border:1px solid; border-radius:10px; border-color:blue; background-color:#EEEEEE;}
	</style>
</head>
<?php 
	$name = $_GET["name"]; 
	$type = $_GET["type"]; 
	$dateStart = $_GET["dateStart"]; 
	$dateEnd = $_GET["dateEnd"]; 
	$r1c1r = $_GET["r1c1r"]; 
	$r1c1o = $_GET["r1c1o"];
	$r1c1v = $_GET["r1c1v"]; 
	$r1c2r = $_GET["r1c2r"]; 
	$r1c2o = $_GET["r1c2o"]; 
	$r1c2v = $_GET["r1c2v"];
	$r2c1r = $_GET["r2c1r"]; 
	$r2c1o = $_GET["r2c1o"]; 
	$r2c1v = $_GET["r2c1v"];
	$r2c2r = $_GET["r2c2r"]; 
	$r2c2o = $_GET["r2c2o"]; 
	$r2c2v = $_GET["r2c2v"];
	$r3c1r = $_GET["r3c1r"]; 
	$r3c1o = $_GET["r3c1o"]; 
	$r3c1v = $_GET["r3c1v"];
	$r3c2r = $_GET["r3c2r"]; 
	$r3c2o = $_GET["r3c2o"]; 
	$r3c2v = $_GET["r3c2v"];
	$r4c1r = $_GET["r4c1r"]; 
	$r4c1o = $_GET["r4c1o"]; 
	$r4c1v = $_GET["r4c1v"]; 
	$r4c2r = $_GET["r4c2r"]; 
	$r4c2o = $_GET["r4c2o"]; 
	$r4c2v = $_GET["r4c2v"];
	$r5c1r = $_GET["r5c1r"]; 
	$r5c1o = $_GET["r5c1o"]; 
	$r5c1v = $_GET["r5c1v"];
	$r5c2r = $_GET["r5c2r"]; 
	$r5c2o = $_GET["r5c2o"]; 
	$r5c2v = $_GET["r5c2v"];
	$r6c1r = $_GET["r6c1r"]; 
	$r6c1o = $_GET["r6c1o"]; 
	$r6c1v = $_GET["r6c1v"];
	$r6c2r = $_GET["r6c2r"]; 
	$r6c2o = $_GET["r6c2o"]; 
	$r6c2v = $_GET["r6c2v"];
	
	
?>
<body>
<h1>Kanthaka</h1>
<h3>Promotion "<?php echo $name; ?>" Successfully added!</h3>

<form name="input" action="index.php" method="get">
<table border="0" width="800" cellpadding="5" cellspacing="0">
<tr><td>
	<table border="0" width="800" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				Promotion Name:
			</td>
			<td>
				<?php echo $name; ?>
			</td>
		</tr>
		<tr>
			<td>
				Promotion Type:
			</td>
			<td>
				<?php echo $type; ?>
			</td>
		</tr>
		<tr>
			<td>
				Running Period:
			</td>
			<td>
				From 15-07-2012 to 15-08-2012
			</td>
		</tr>
		<tr>
			<td>
				Promotion Rules:
			</td>
			<td>
				<?php 
				if ($r1c1r!="(Select)") {
					echo "( ".$r1c1r." ".$r1c1o." ".$r1c1v;
					if ($r1c2r!="(Select)") echo " || ".$r1c2r." ".$r1c2o." ".$r1c2v." )"; else echo " )";
				}
				if ($r2c1r!="(Select)") {
					echo " && ( ".$r2c1r." ".$r2c1o." ".$r2c1v;
					if ($r2c2r!="(Select)") echo " || ".$r2c2r." ".$r2c2o." ".$r2c2v." )"; else echo " )";
				}
				if ($r3c1r!="(Select)") {
					echo " && ( ".$r3c1r." ".$r3c1o." ".$r3c1v;
					if ($r3c2r!="(Select)") echo " || ".$r3c2r." ".$r3c2o." ".$r3c2v." )"; else echo " )";
				}
				if ($r4c1r!="(Select)") {
					echo " && ( ".$r4c1r." ".$r4c1o." ".$r4c1v;
					if ($r4c2r!="(Select)") echo " || ".$r4c2r." ".$r4c2o." ".$r4c2v." )"; else echo " )";
				}
				if ($r5c1r!="(Select)") {
					echo " && ( ".$r5c1r." ".$r5c1o." ".$r5c1v;
					if ($r5c2r!="(Select)") echo " || ".$r5c2r." ".$r5c2o." ".$r5c2v." )"; else echo " )";
				}
				if ($r6c1r!="(Select)") {
					echo " && ( ".$r6c1r." ".$r6c1o." ".$r6c1v;
					if ($r6c2r!="(Select)") echo " || ".$r6c2r." ".$r6c2o." ".$r6c2v." )"; else echo " )"; 
				}
				?>
			</td>
		</tr>
	</table>
</td></tr>

<tr><td>
	<table border="0" width="800" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<br><input type="submit" value="Add another Promotion" /><br>
			</td>
			<td>
			</td>
		</tr>
	</table>
</td></tr>
</table>
</form>
</body>
</html>