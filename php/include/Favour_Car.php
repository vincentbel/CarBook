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
			$query = "SELECT brand.name, brand_series.name, car.model_number, pictures_url, price_highest, price_lowest FROM car JOIN brand USING(brand_id) JOIN brand_series USING (series_id)  ORDER BY view_times desc LIMIT 10";
			$result = mysqli_query($this->dbc,$query);
			$this->check_sql_error($this->dbc,$query,$result);

			$counter = 0;

			if($result){
				while($row = mysqli_fetch_array($result)){
					$rate_array[$counter]["brand"] = $row[0];
					$rate_array[$counter]["brand_series"] = $row[1];
					$rate_array[$counter]["model_number"] = $row[2];
					$rate_array[$counter]["pictures_url"] = $row[3]."/1.jpg";
					$rate_array[$counter]["price"] = ($row["price_lowest"]/10000)."万-".($row["price_highest"]/10000)."万";
					$counter++;
				}
				return $rate_array;
			}
			else{
				return false;
			}
		}

	}
?>