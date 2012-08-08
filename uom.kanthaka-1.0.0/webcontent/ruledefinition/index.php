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
		.rowBorder {border:1px solid; border-radius:10px; border-color:grey; background-color:#EEEEEE;}
	</style>
</head>
<body>
<h1>Kanthaka</h1>
<h3>Add a New Promotion</h3>

<form name="input" action="success.php" method="get">
<table border="0" width="600" cellpadding="5" cellspacing="0">
<tr><td>
	<table border="0" width="600" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				Promotion Name:
			</td>
			<td>
				<input type="text" name="name" size="30"/>
			</td>
		</tr>
		<tr>
			<td>
				Promotion Type:
			</td>
			<td>
				<select name="type">
					<option>Local</option>
					<option>Coorperate</option>
					<option>IDD</option>
					<option>Roaming</option>
					<option>Seasonal</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				Running Period:
			</td>
			<td>
				<p id="dateRange"  name="date"></p>
				<script type="text/javascript">
					$('#dateRange').DatePicker({
						flat: true,
						date: ['2012-07-10','2012-08-10'],
						current: '2012-08-31',
						calendars: 2,
						mode: 'range',
						starts: 1
					});
				</script>
			</td>
		</tr>
		<tr>
			<td>
				Promotion Rules:
			</td>
			<td>
			</td>
		</tr>
	</table>
</td></tr>
<tr><td>
	
	<div class="rowBorder">
	<table border="0" width="600" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<select name="r1c1r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r1c1o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r1c1v" size="15"/>
			</td>
			<td class="or">
				OR
			</td>
			<td>
				<select name="r1c2r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r1c2o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r1c2v" size="15"/>
			</td>
		</tr>
	</table>
	</div>
</td></tr>

<tr><td class="and">
	<center>AND</center>
</td></tr>

<tr><td>
	<div class="rowBorder">
	<table border="0" width="600" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<select name="r2c1r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r2c1o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r2c1v" size="15"/>
			</td>
			<td class="or">
				OR
			</td>
			<td>
				<select name="r2c2r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r2c2o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r2c2o" size="15"/>
			</td>
		</tr>
	</table>
	</div>
</td></tr>

<tr><td class="and">
	<center>AND</center>
</td></tr>

<tr><td>
	<div class="rowBorder">
	<table border="0" width="600" cellpadding="5" cellspacing="0">
			<tr>
			<td>
				<select name="r3c1r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r3c1o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r3c1v" size="15"/>
			</td>
			<td class="or">
				OR
			</td>
			<td>
				<select name="r3c2r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r3c2o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r3c2v" size="15"/>
			</td>
		</tr>
	</table>
	</div>
</td></tr>

<tr><td class="and">
	<center>AND</center>
</td></tr>

<tr><td>
	<div class="rowBorder">
	<table border="0" width="600" cellpadding="5" cellspacing="0">
			<tr>
			<td>
				<select name="r4c1r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r4c1o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r4c1v" size="15"/>
			</td>
			<td class="or">
				OR
			</td>
			<td>
				<select name="r4c2r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r4c2o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r4c2v" size="15"/>
			</td>
		</tr>
	</table>
	</div>
</td></tr>

<tr><td class="and">
	<center>AND</center>
</td></tr>

<tr><td>
	<div class="rowBorder">
	<table border="0" width="600" cellpadding="5" cellspacing="0">
			<tr>
			<td>
				<select name="r5c1r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r5c1o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r5c1v" size="15"/>
			</td>
			<td class="or">
				OR
			</td>
			<td>
				<select name="r5c2r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r5c2o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r5c2v" size="15"/>
			</td>
		</tr>
	</table>
	</div>
</td></tr>

<tr><td class="and">
	<center>AND</center>
</td></tr>

<tr><td>
	<div class="rowBorder">
	<table border="0" width="600" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<select name="r6c1r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r6c1o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r6c1v" size="15"/>
			</td>
			<td class="or">
				OR
			</td>
			<td>
				<select name="r6c2r">
					<option>(Select)</option>
					<option>Called No</option>
					<option>SMSed No</option>
					<option>No of Calls</option>
					<option>No of SMSs</option>
					<option>Call Duration</option>
					<option>Connection Type</option>
				</select>
			</td>
			<td>
				<select name="r6c2o">
					<option>=</option>
					<option>></option>
					<option><</option>
					<option>Contains</option>
					<option>Starts with</option>
					<option>Ends with</option>
				</select>
			</td>
			<td>
				<input type="text" name="r6c2v" size="15"/>
			</td>
		</tr>
	</table>
	</div>
</td></tr>

<tr><td>
	<table border="0" width="600" cellpadding="5" cellspacing="0">
		<tr>
			<td>
				<br><input type="submit" value="Add Promotion" /><br>
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