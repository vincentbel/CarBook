<?php
	class User_Setting{
		private $db;
		private $dbc;

		//constructor
		function __construct(){
			require_once 'DB_Connect.php';
			//connecting to database;
			$this->db = new DB_Connect();
			$this->dbc = $this->db->connect();
		}

		//destructor
		function __destruct(){

		}


	/*
		Check SQL error and echo the error message
	*/

		public function check_sql_error($dbc, $query, $data) {
			if (!$data) {
	   			printf("Error: %s\n", mysqli_error($dbc));
	    		echo "\n".$query;
	    		mysqli_close($dbc);
	    		exit();
			}
		}

		
		/*
			Test success.
			Update user's basic infomation.
		*/
		public function update_infomation($username,$email,$password,$gender,$birthdate){
			$hash = $this->hashSSHA($password);
			$encrypted_password = $hash["encrypted"]; // encrypted password
			$salt = $hash["salt"]; // salt

			
			$query = "UPDATE user SET encrypted_password='$encrypted_password',salt='$salt',email='$email',updated_at=NOW(),gender=$gender,birthdate='$birthdate' WHERE username='$username'";
		
			$result = mysqli_query($this->dbc, $query);
			
			
			// checked for successful store
			$this->check_sql_error($this->dbc, $query, $result);

			if ($result) {
				// get user details
				
				$query = "SELECT * FROM user WHERE username='$username'";
				$result = mysqli_query($this->dbc, $query);
				// return user details
				return mysqli_fetch_array($result);
			}else {
				return false;
			}
		}

		public function verify_password($user_id, $password) {
			$query = "SELECT * FROM user WHERE user_id = $user_id";
			$result = mysqli_query($this->dbc, $query);
			// check the query
			$this->check_sql_error($this->dbc, $query, $result);
			$no_of_rows = mysqli_num_rows($result);
			if ($no_of_rows > 0) {
				$result = mysqli_fetch_array($result);
				$salt = $result['salt'];
				$encrypted_password = $result['encrypted_password'];
				$hash = $this->checkhashSSHA($salt, $password);
				// check for password equality
				if ($encrypted_password == $hash) {
					return true;
				}
			} else {
				return false;
			}
		}

		public function change_password($user_id, $password) {
			$hash = $this->hashSSHA($password);
			$encrypted_password = $hash["encrypted"]; // encrypted password
			$salt = $hash["salt"]; // salt
		
			$query = "UPDATE user SET encrypted_password = '$encrypted_password',salt = '$salt' WHERE user_id = $user_id";
			$result = mysqli_query($this->dbc, $query);

			if ($result) {
				return true;
			} else {
				return false;
			}
		}

		public function update_avatar($user_id, $status) {
			$query = "UPDATE user SET avatar_status = $status WHERE user_id = $user_id";
			$result = mysqli_query($this->dbc, $query);
			// check the query
			$this->check_sql_error($this->dbc, $query, $result);
			if ($result) {
				return true;
			} else {
				return false;
			}
		}

		/*
		Has not tested yet.
		*/
		public function update_user_favour_type($username,$favourBrandId,$grade,$bodyStructure,$affordablePriceLowest,$affordablePriceHighest){
			$res = mysqli_query($this->dbc,"SELECT user_id FROM user WHERE username='$username'");
			$row = mysqli_fetch_array($res);
			$user_id = $row['user_id'];
			$query = "UPDATE user_favour_type SET favour_brand_id=$favourBrandId,grade='$grade',body_structure='$bodyStructure',affordable_price_lowest=$affordablePriceLowest,affordable_price_highest=$affordablePriceHighest WHERE user_id = $user_id";
			$result = mysqli_query($this->dbc, $query);
			// checked for successful store
			$this->check_sql_error($this->dbc, $query, $result);
			if ($result) {
				// get user_favour_type details
				$query = "SELECT * FROM user_favour_type WHERE user_id = $user_id";
				$result = mysqli_query($this->dbc, $query);
				// return user_favour_type details
				
				return mysqli_fetch_array($result);
			}else {
				return false;
			}
		}

		public function hashSSHA($password) {

			$salt = sha1(rand());
			$salt = substr($salt, 0, 10);
			$encrypted = base64_encode(sha1($password . $salt, true). $salt);
			$hash = array("salt" => $salt, "encrypted" => $encrypted);
			return $hash;
		}

		public function checkhashSSHA($salt, $password) {
			
			$hash = base64_encode(sha1($password . $salt, true). $salt);
			return $hash;
		}
	}
?>