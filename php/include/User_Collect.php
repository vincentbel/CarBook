<?php
	class User_Collect{
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

		public function check_sql_error($dbc, $query, $data) {
			if (!$data) {
	   			printf("Error: %s\n", mysqli_error($dbc));
	    		echo "\n".$query;
	    		mysqli_close($dbc);
	    		exit();
			}
		}

		public function collect_cars($user_id,$car_id){
			$query = "INSERT INTO user_collect(user_id,car_id,collect_time) VALUES ($user_id,$car_id,NOW())";
			$result = mysqli_query($this->dbc,$query);
			$this->check_sql_error($this->dbc,$query,$result);
			if($result){
				$c_id = mysqli_insert_id($this->dbc);
				$query = "SELECT * FROM user_collect WHERE collect_id = $c_id";
				$result = mysqli_query($this->dbc,$query);
				return mysqli_fetch_array($result);
			}else{
				return false;
			}
		}

		public function delete_collected_cars($user_id,$car_id){
			$query = "SELECT collect_id FROM user_collect WHERE user_id = $user_id and car_id=$car_id";
			$res = mysqli_query($this->dbc,$query);
			$row = mysqli_fetch_array($res);
			$delete_id = $row['collect_id'];
			$query = "DELETE FROM user_collect WHERE collect_id=$delete_id";
			$result = mysqli_query($this->dbc,$query);
			if ($result){
				return true;
			}else{
				return false;
			}
		}

	}

?>
