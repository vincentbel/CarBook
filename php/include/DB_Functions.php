<?php
	class DB_Functinos {
		private $db;

		// constructor
		function __construct() {
			require_once 'DB_Connect.php';
			// connecting to database
			$this->db = new DB_Connect();
			$this->db->connect();
		}

		// destructor
		function __destruct() {

		}
	/*                         
		Storing new user
		return user details
    */
		public function storeUser($username, $email, $password) {
			$hash = $this->hashSSHA($password);
			$encrypted_password = $hash["encrypted"]; // encrypted password
			$salt = $hash["salt"]; // salt
			$result = mysqli_query("INSERT INTO user(username, password, salt, created_at) VALUES ('$username', '$encrypted_password', '$salt', NOW())");
			// checked for successful store
			if ($result) {
				// get user details
				$uid = mysqli_insert_id(); // last inserted id
				$result = mysqli_query("SELECT * FROM user WHERE user_id = $uid");
				// return user details
				return mysqli_fetch_array($result);
			}else {
				return false;
			}
		}
	/*
		Get user by email and password
	*/

		public function getUserByEmailAndPassword($email, $password) {
			$result = mysqli_query("SELECT * FROM user WHERE email = '$email'") or die(mysqli_error());
			// check for result
			$no_of_rows = mysqli_num_rows($result);
			if ($no_of_rows > 0) {
				$result = mysqli_fetch_array($result);
				$salt = $result['$salt'];
				$encrypted_password = $result['encrypted_password'];
				$hash = $this->checkhashSSHA($password, $salt);
				// check for password equality
				if ($encrypted_password == $hash) {
					// user authentication details are correct
					return $result;
				}
			} else {
				// user not found
				return false;
			}
		}

	/*
		Get user by username and password
	*/
		public function getUserByUsernameAndPassword($username,$password) {
			$result = mysqli_query("SELECT * FROM user WHERE username = '$username'") or die(mysqli_error());
			// check for result 
			$no_of_rows = mysqli_num_rows($result);
			if ($no_of_rows > 0) {
				$result = mysqli_fetch_array($result);
				$salt = $result['$salt'];
				$encrypted_password = $result['encrypted_password'];
				$hash = $this->checkhashSSHA($password,$salt);
				// check for password equality
				if ($encrypted_password == $hash) {
					// user authentication details are correct
					return $result;
				} 
			} else {
				// user not found
				return false;
			}
		}



	/*
		Check user is existed or not by email
	*/
	
		public function isUserExistedByEmail($email) {
			$result = mysqli_query("SELECT * FROM user WHERE email = '$email'") or die(mysqli_error());
			// check for result
			$no_of_rows = mysqli_num_rows($result);
			if ($no_of_rows > 0) {
				// user existed
				return true;
			} else {
				// user not existed
				return false;
			}
		}
	
	/*
		Check user is existed or not by username
	*/

		public function isUserExistedByUsername($username) {
			$result = mysqli_query("SELECT * FROM user WHERE username = '$username'") or die(mysqli_error());
			// check for result
			$no_of_rows = mysqli_num_rows($result);
			if ($no_of_rows > 0) {
				// user existed
				return true;
			} else {
				// user not existed
				return false;
			}
		}

		/**
		*Encrypting password
		*@param password
		*return salt and 
		*/

		public function hashSSHA($password) {

			$salt = sha1(rand());
			$salt = substr($salt, 0, 10);
			$encrypted = base64_encode(sha1($password . $salt, true). $salt);
			$hash = array("salt" => $salt, "encrypted" => $encrypted);
			return $hash;
		}
	
		/**
	 	*Dencrypting password
	 	*@param salt, password
		*return hash string
		*/
	
		public function checkhashSSHA($salt, $password) {
			
			$hash = base64_encode(sha1($password . $salt, true). $salt);
			return $hash;
		}
	}
?>