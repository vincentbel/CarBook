<?php
	// 实现用户注册、登陆功能

	class DB_Functions {
		private $db;
		private $dbc;

		// constructor
		function __construct() {
			require_once 'DB_Connect.php';
			// connecting to database
			$this->db = new DB_Connect();
			$this->dbc = $this->db->connect();
		}

		// destructor
		function __destruct() {

		}

		// close database connection
		public function close_dbc() {
			$this->db->close();
		}

	/**
	 *	Check SQL error and echo the error message
	 */

		public function check_sql_error($dbc, $query, $data) {
			if (!$data) {
	   			printf("Error: %s\n", mysqli_error($dbc));
	    		echo "\n".$query;
	    		// close connection
	    		$this->close_dbc();
	    		exit();
			}
		}



	/**                         
	 *	Storing new user
	 *	return user details
     */
		public function storeUser($username, $email, $password) {

			$hash = $this->hashSSHA($password);
			$encrypted_password = $hash["encrypted"]; // encrypted password
			$salt = $hash["salt"]; // salt
			$query = "INSERT INTO user(username, encrypted_password, email, salt, created_at) 
						VALUES ('$username', '$encrypted_password', '$email', '$salt', NOW())";
			$result = mysqli_query($this->dbc, $query);
			// checked for successful store
			$this->check_sql_error($this->dbc, $query, $result);
			if ($result) {
				// get user id
				$uid = mysqli_insert_id($this->dbc); // last inserted id
				// init user_favour_type
				$query = "INSERT INTO user_favour_type(user_id) VALUES ($uid)";
				$result = mysqli_query($this->dbc, $query);
				// checked for successful store 
				$this->check_sql_error($this->dbc, $query, $result);				

				$query = "SELECT * FROM user WHERE user_id = $uid";
				$result = mysqli_query($this->dbc, $query);
				// checked for successful store 
				$this->check_sql_error($this->dbc, $query, $result);
				// return user details
				return mysqli_fetch_array($result);
			}else {
				return false;
			}
		}
	/**
	 *	Get user by email and password
	 */

		public function getUserByEmailAndPassword($email, $password) {			

			$query = "SELECT * FROM user WHERE email = '$email'";
			$result = mysqli_query($this->dbc, $query) or die(mysqli_error($this->dbc));
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

	/**
	 *	Get user by username and password
	 */
		public function getUserByUsernameAndPassword($username, $password) {			

			$query = "SELECT * FROM user WHERE username = '$username'";
			$result = mysqli_query($this->dbc, $query);
			// check for result 
			$this->check_sql_error($this->dbc, $query, $result);
			$no_of_rows = mysqli_num_rows($result);
			if ($no_of_rows > 0) {
				$result = mysqli_fetch_array($result);
				$salt = $result['salt'];
				$encrypted_password = $result['encrypted_password'];
				$hash = $this->checkhashSSHA($salt, $password);
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



	/**
	 *	Check user is existed or not by email
	 */
	
		public function isUserExistedByEmail($email) {			

			$query = "SELECT * FROM user WHERE email = '$email'";
			$result = mysqli_query($this->dbc,$query) or die(mysqli_error($this->dbc));
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
	 *	Check user is existed or not by username
	 */

		public function isUserExistedByUsername($username) {

			$query = "SELECT * FROM user WHERE username = '$username'";
			$result = mysqli_query($this->dbc, $query) or die(mysqli_error($this->dbc));
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
		 *return salt and encrypted_password
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