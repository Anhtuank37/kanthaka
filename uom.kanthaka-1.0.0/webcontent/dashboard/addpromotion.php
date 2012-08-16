<?php
include('lock.php');
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
	<script type="text/javascript" src="js/jquery-ui.min.js"></script>
	<link rel="Stylesheet" href="css/jquery-ui.css" type="text/css">

	
	<link rel="Stylesheet" href="css/ui.selectmenu.css" type="text/css">
	<script type="text/javascript" src="js/ui.selectmenu.js"></script>
	<link rel="Stylesheet" href="http://jqueryui.com/themeroller/css/parseTheme.css.php?ffDefault=Verdana,Arial,sans-serif&fwDefault=normal&fsDefault=1.1em&cornerRadius=4px&bgColorHeader=cccccc&bgTextureHeader=03_highlight_soft.png&bgImgOpacityHeader=75&borderColorHeader=aaaaaa&fcHeader=222222&iconColorHeader=222222&bgColorContent=ffffff&bgTextureContent=01_flat.png&bgImgOpacityContent=75&borderColorContent=aaaaaa&fcContent=222222&iconColorContent=222222&bgColorDefault=e6e6e6&bgTextureDefault=02_glass.png&bgImgOpacityDefault=75&borderColorDefault=d3d3d3&fcDefault=555555&iconColorDefault=888888&bgColorHover=dadada&bgTextureHover=02_glass.png&bgImgOpacityHover=75&borderColorHover=999999&fcHover=212121&iconColorHover=454545&bgColorActive=ffffff&bgTextureActive=02_glass.png&bgImgOpacityActive=65&borderColorActive=aaaaaa&fcActive=212121&iconColorActive=454545&bgColorHighlight=fbf9ee&bgTextureHighlight=02_glass.png&bgImgOpacityHighlight=55&borderColorHighlight=fcefa1&fcHighlight=363636&iconColorHighlight=2e83ff&bgColorError=fef1ec&bgTextureError=02_glass.png&bgImgOpacityError=95&borderColorError=cd0a0a&fcError=cd0a0a&iconColorError=cd0a0a&bgColorOverlay=aaaaaa&bgTextureOverlay=01_flat.png&bgImgOpacityOverlay=0&opacityOverlay=30&bgColorShadow=aaaaaa&bgTextureShadow=01_flat.png&bgImgOpacityShadow=0&opacityShadow=30&thicknessShadow=8px&offsetTopShadow=-8px&offsetLeftShadow=-8px&cornerRadiusShadow=8px" type="text/css">
	

	<script type="text/javascript">
		$(function(){
			$('select').selectmenu({style:'dropdown'});
			$('.mini').datepicker();
			showBasic();
		});
	</script>
	
</head>

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
			<li id="tbasic" class="active"><a href="javascript:showBasic()" title="Basic"><span>Basic</span></a></li>
			<li id="trules"><a href="javascript:showRules()" title="Rules"><span>Rules</span></a></li>
		</ul>
		
		<form method="post" action="success.php">
		<fieldset>
			<div id="basic">
				<label>Promotion Name:</label>
				<span class="small_input"><input class="small" name="name" type="text" value="" /></span><br class="hid" />
				<!--<span class="negative"></span><br class="hid" />
				<span class="positive"></span><br class="hid" />-->
				
				<label>Promotion Type:</label>
				<select name="type"  style="width: 247px;">
					<option value="Local">Local</option>
					<option value="Corporate">Corporate</option>
					<option value="IDD" >IDD</option>
					<option value="Roaming">Roaming</option>
					<option value="Seasonal">Seasonal</option>
				</select>
				<br class="hid" />
				
				<label>Running Period:</label>
				<input class="mini" placeholder="Start date" name="dateStart" type="text">
				<input class="mini" placeholder="End date" name="dateEnd" type="text">
				<p>Select Start Date & End Date of the promotion </p>
			
				<label>Description:</label><br class="hid" />
				<span class="textarea"><textarea name="description" cols="30" rows="4"></textarea></span><br class="hid" />
				
				<a href="javascript:showRules();" class="button" title="Next"><span>Next</span></a>
				<span class="clear"></span>
				
			</div>
			
			<div id="rules">
				<label>Promotion Rules:</label>
				<span class="ruletable">
				<table width="400" border="0" cellspacing="0">
					<tr>
						<td>			
						<div class="rowBorder">
						<table cellspacing="0">
							<tr>
								<td>
									<select name="r1c1r" >
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r1c1o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r1c1v" />
								</td>
								<td class="or">
									OR
								</td>
								<td>
									<select name="r1c2r">
										<option value="">(Select)</option>
										<option>Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r1c2o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r1c2v" />
								</td>
							</tr>
						</table>
						</div>
					</tr>	
					
					<tr>
						<td class="and">
							<center>AND</center>
						</td>
					</tr>

					<tr>
						<td>
						<div class="rowBorder">
						<table cellspacing="0">
							<tr>
								<td>
									<select name="r2c1r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r2c1o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r2c1v" size="15"/>
								</td>
								<td class="or">
									OR
								</td>
								<td>
									<select name="r2c2r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r2c2o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r2c2o" size="15"/>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>

					<tr>
						<td class="and">
							<center>AND</center>
						</td>
					</tr>

					<tr>
						<td>
						<div class="rowBorder">
						<table cellspacing="0">
							<tr>
								<td>
									<select name="r3c1r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r3c1o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r3c1v" size="15"/>
								</td>
								<td class="or">
									OR
								</td>
								<td>
									<select name="r3c2r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r3c2o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r3c2v" size="15"/>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>

					<tr>
						<td class="and">
							<center>AND</center>
						</td>
					</tr>

					<tr>
						<td>
						<div class="rowBorder">
						<table cellspacing="0">
							<tr>
								<td>
									<select name="r4c1r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r4c1o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r4c1v" size="15"/>
								</td>
								<td class="or">
									OR
								</td>
								<td>
									<select name="r4c2r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r4c2o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r4c2v" size="15"/>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>

					<tr>
						<td class="and">
							<center>AND</center>
						</td>
					</tr>

					<tr>
						<td>
						<div class="rowBorder">
						<table cellspacing="0">
							<tr>
								<td>
									<select name="r5c1r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r5c1o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r5c1v" size="15"/>
								</td>
								<td class="or">
									OR
								</td>
								<td>
									<select name="r5c2r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r5c2o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r5c2v" size="15"/>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>

					<tr>
						<td class="and">
							<center>AND</center>
						</td>
					</tr>

					<tr>
						<td>
						<div class="rowBorder">
						<table cellspacing="0">
							<tr>
								<td>
									<select name="r6c1r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r6c1o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r6c1v" size="15"/>
								</td>
								<td class="or">
									OR
								</td>
								<td>
									<select name="r6c2r">
										<option value="">(Select)</option>
										<option value="Dest_No">Dest No</option>
										<option value="No_of_Calls">No of Calls</option>
										<option value="No_of_SMSs">No of SMSs</option>
										<option value="Call_Duration">Call Duration</option>
										<option value="Connection_Type">Connection Type</option>
									</select>
								</td>
								<td>
									<select name="r6c2o">
										<option value="="> = </option>
										<option value=">"> > </option>
										<option value="<"> < </option>
										<option value="Starts_with">Starts with</option>
									</select>
								</td>
								<td>
									<input class="minix" type="text" name="r6c2v" size="15"/>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
				</table>
				</span>
				
				<a href="javascript:showBasic();" class="button" title="Back"><span>Back</span></a>
				<a href="#" class="submit button" title="Save Promotion"><span>Save Promotion</span></a><span class="clear"></span>
				
			</div>
			
		</fieldset>
		</form>
		
		
	</div>
	<div id="footer">
	</div>
</div>

</body>

</html>
