<?php
class DB_Connect {
	// constructor
	function __construct(){

	}

	// destructor
	function __destruct(){

	}

	// Connecting to database
	public function connect() {
		require_once 'config.php';
		// connecting to mysql
		$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE) or die("Cannot connect to database.");
		
		// return database handler
		return $con;
	}

	public function close() {
		mysqli_close();
	}

}
?>