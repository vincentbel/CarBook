<?php
class DB_Connect {
	private $con;

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
		$this->con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE) or die("Cannot connect to database.");
		mysqli_query($this->con, "SET NAMES 'utf8'");
		// return database handler
		return $this->con;
	}

	public function close() {
		mysqli_close($this->con);
	}

	
}
?>