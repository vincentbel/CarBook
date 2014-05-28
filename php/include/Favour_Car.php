<?php
	//热门汽车排行
	
	class Favour_Car{
		private $db;
		private $dbc;

		function __construct(){
			require_once 'DB_Connect.php';
			$this->db = new DB_Connect();
			$this->dbc = $this->db->connect();
		}

		function __destruct(){

		}

		public function close_dbc(){
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

		public function get_car_rate(){
			$rate_array = array();
			$query = "SELECT brand.name,brand_series.name,car.model_number FROM car JOIN brand USING(brand_id) JOIN brand_series USING (series_id)  ORDER BY view_times desc LIMIT 10";
			$result = mysqli_query($this->dbc,$query);
			$this->check_sql_error($this->dbc,$query,$result);
			if($result){
				$row = mysqli_fetch_array($result);
				while($row){
					array_push($rate_array, $row);
					$row = mysqli_fetch_array($result);
				}
				return $rate_array;
			}
			else{
				return false;
			}
		}

	}
?>