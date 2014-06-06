<?php
	class User_Comments{
		private $db;
		private $dbc;

		//constructor
		function __construct(){
			require_once 'DB_Connect.php';
			$this->db = new DB_Connect();
			$this->dbc = $this->db->connect();
		}

		//destructor
		function __destruct(){

		}
		

		// close database connection
		public function close_dbc() {
			$this->db->close();
		}

		public function check_sql_error($query, $data) {
			if (!$data) {
	   			printf("Error: %s\n", mysqli_error($this->dbc));
	    		echo "\n".$query;
	    		mysqli_close($this->dbc);
	    		exit();
			}
		}

		public function get_all_comments($car_id) {
			$query = "SELECT `username`, `short_comments`, `comment_time`, `rate` FROM `user_comments` INNER JOIN `user` USING(`user_id`) WHERE `car_id` = $car_id";
			$result = mysqli_query($this->dbc,$query);
			$this->check_sql_error($query, $result);
			$rows = array();
			while ($row = mysqli_fetch_array($result)) {
				array_push($rows, $row);
			}
			return $rows;
		}

		public function get_my_comments($user_id) {
			$query = "SELECT * FROM `user_comments` WHERE `user_id` = $user_id";
			$result = mysqli_query($this->dbc,$query);
			$this->check_sql_error($query, $result);
			$rows = array();
			while ($row = mysqli_fetch_array($result)) {
				array_push($rows, $row);
			}
			return $rows;
		}

		public function add_comment($username, $car_id, $comment, $comment_time, $rate) {
			$query0 = "SELECT * FROM `user` WHERE `username` = '$username' LIMIT 1";
			$result0 = mysqli_query($this->dbc, $query0);
			$this->check_sql_error($query0, $result0);
			if (mysqli_num_rows($result0) == 0) {
				echo "no usrename found!";
				exit(0);
			}
			$row = mysqli_fetch_array($result0);
			$user_id = $row['user_id'];
			$query = "INSERT INTO `user_comments`(`user_id` ,`car_id` ,`short_comments` ,`comment_time` ,`rate`) VALUES($user_id, $car_id, '$comment', '$comment_time', $rate)";
			$result = mysqli_query($this->dbc, $query);
			$this->check_sql_error($query, $result);
		}

		public function delete_comment($username, $car_id, $comment) {
			$query0 = "SELECT * FROM `user` WHERE `username` = '$username' LIMIT 1";
			$result0 = mysqli_query($this->dbc, $query0);
			$this->check_sql_error($query0, $result0);
			if (mysqli_num_rows($result0) == 0) {
				echo "no usrename found!";
				exit(0);
			}
			$row = mysqli_fetch_array($result0);
			$user_id = $row['user_id'];
			$query = "DELETE FROM `user_comments` WHERE `user_id` = $user_id AND `car_id` = $car_id AND `short_comments`= '$comment' LIMIT 1";
			$result = mysqli_query($this->dbc,$query);
			$this->check_sql_error($query, $result);
			return mysqli_affected_rows($this->dbc);
		}
	}

?>
