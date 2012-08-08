<?php
include('lock.php');
include('config.php');

$id = $_POST["id"]; 

$result = mysql_query("DELETE FROM promotions WHERE name='$id'");

header("Location: viewpromotions.php");

?>
