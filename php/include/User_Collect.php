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
		

		// close database connection
		public function close_dbc() {
			$this->db->close();
		}

		public function check_sql_error($dbc, $query, $data) {
			if (!$data) {
	   			printf("Error: %s\n", mysqli_error($dbc));
	    		echo "\n".$query;
	    		mysqli_close($dbc);
	    		exit();
			}
		}

		public function collect_cars($user_id, $car_id){
			$query = "SELECT collect_id FROM user_collect WHERE user_id = $user_id and car_id=$car_id";
			$result = mysqli_query($this->dbc,$query);
			if (!$result) {
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
			} else {
				return true;
			}
		}

		public function delete_collected_cars($user_id,$car_id) {
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

		public function show_collected_cars($user_id) {
			$_query = "SELECT car_id FROM user_collect WHERE user_id = $user_id";
			$_result = mysqli_query($this->dbc, $_query);
			$this->check_sql_error($this->dbc, $_query, $_result);
			$collect_information;
			$counter = 0;
			while ($row = mysqli_fetch_row($_result)) {
				// query car's grade
				$car_id = $row[0];
				$query = "SELECT car_id, grade FROM car NATURAL JOIN car_grade WHERE car.car_id = $car_id";
				$result = mysqli_query($this->dbc, $query);
				$result = mysqli_fetch_array($result);
				$collect_information[$counter]["car_id"] = $result["car_id"];
				$collect_information[$counter]["grade"] = $result["grade"];

				// query car's price
				$query = "SELECT price_highest, price_lowest FROM car WHERE car_id = $car_id";
				$result = mysqli_query($this->dbc, $query);
				$result = mysqli_fetch_array($result);
				$collect_information[$counter]["price"] = ($result["price_lowest"]/10000)."万-".($result["price_highest"]/10000)."万"; 
			
				// query car's brand, brand series, model_number
				$query = "SELECT brand.name, brand_series.name, model_number FROM car JOIN brand USING (brand_id) JOIN brand_series USING (series_id) WHERE car.car_id = $car_id";
				$result = mysqli_query($this->dbc, $query);
				$result = mysqli_fetch_array($result);
				$collect_information[$counter]["brand"] = $result[0];
				$collect_information[$counter]["brand_series"] = $result[1];
				$collect_information[$counter]["model_number"] = $result[2];
				
				// query car's pictures' url
				$query = "SELECT pictures_url FROM car WHERE car_id = $car_id";
				$result = mysqli_query($this->dbc, $query);
				$result = mysqli_fetch_array($result);
				$url = $result["pictures_url"]."/1.jpg";
				$collect_information[$counter]["pictures_url"] = $url;

				$counter++;
			}
			$collect_information["number"] = $counter;

			return $collect_information;
		}

		// get car_information by car_id
		public function get_information_by_car_id($car_id) {
			$query = "SELECT car_id, brand.name, brand_series.name, model_number, price_highest, price_lowest, pictures_url FROM car JOIN brand USING (brand_id) JOIN brand_series USING (series_id) WHERE car.car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);

			$car["car_id"] = $result[0];
			$car["brand_name"] = $result[1];
			$car["brand_series"] = $result[2];
			$car["model_number"] = $result[3];
			$car["price"] = ($result["price_lowest"]/10000)."万-".($result["price_highest"]/10000)."万";
			$car["pictures_url"] = $result["pictures_url"]."/1.jpg";

			$query = "SELECT grade FROM car NATURAL JOIN car_grade WHERE car.car_id = $car_id";			
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$car["grade"] = $result[0];

			return $car;
		}

	}

?>
