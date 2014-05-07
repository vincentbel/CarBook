<?php
	// 实现汽车搜索功能

	class Car_Search {
		private $db;
		private $dbc;

		private $brand;
		private $series;

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
	
	// get car_brand

		public function get_car_brand() {
			$query = "SELECT name FROM brand";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			// counter of brand
			$counter = 0;
			while ($row = mysqli_fetch_row($result)) {
				$brand[$counter] = $row[0];
				$brand[$counter."_url"] = "picture/".$row[0]."/brand.jpg";
				$counter++;
			}
			$brand["number"] = $counter;
			return $brand;
		}

	// get brand_series by car brand

		public function get_brand_series($brand) {
			$query = "SELECT brand_series.name FROM brand, brand_series WHERE brand.name = '$brand' AND brand.brand_id = brand_series.brand_id";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			// counter of brand_series
			$counter = 0;
			while ($row = mysqli_fetch_row($result)) {
				$brand_series[$counter++] = $row[0];
			}
			$brand_series["number"] = $counter;
			return $brand_series;
		}
	
	// get car model_number by car brand and brand_series 
	    public function get_model_number($brand_series) {
	    	$query = "SELECT model_number FROM car JOIN brand_series USING (series_id) WHERE brand_series.name = '$brand_series'";
	    	$result = mysqli_query($this->dbc, $query);
	    	// check the query result
	    	$this->check_sql_error($this->dbc, $query, $result);
	    	// counter of model_number
	    	$counter = 0;
	    	while ($row = mysqli_fetch_row($result)) {
	    		$model_number[$counter++] = $row[0];
	    	}
	    	$model_number["number"] = $counter; 
	    	return $model_number;
	    }
	
	// get car betweent low_price and high_price
		public function get_car_between_prices($low_price, $high_price) {
			$query = "SELECT  brand.name, brand_series.name, model_number, pictures_url, grade FROM car JOIN brand USING (brand_id) JOIN brand_series USING (series_id) JOIN car_grade USING (car_grade_id) WHERE price_lowest >= $low_price AND price_highest <= $high_price";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			// counter of brand_series
			$counter = 0;
			while ($row = mysqli_fetch_row($result)) {
				$car_information[$counter]["brand"] = $row[0];
				$car_information[$counter]["brand_series"] = $row[1];
				$car_information[$counter]["model_number"] = $row[2];
				$car_information[$counter]["pictures_url"] = $row[3]."/1.jpg";
				$car_information[$counter]["grade"] = $row[4];
				$counter++;
			}
			$car_information["number"] = $counter;
			return $car_information;
		}

	// get car by grade
		public function get_car_by_grade($grade) {
			$query = "SELECT  brand.name, brand_series.name, model_number, pictures_url, grade FROM car JOIN brand USING (brand_id) JOIN brand_series USING (series_id) JOIN car_grade USING (car_grade_id) WHERE grade = '$grade'";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			// counter of brand_series
			$counter = 0;
			while ($row = mysqli_fetch_row($result)) {
				$car_information[$counter]["brand"] = $row[0];
				$car_information[$counter]["brand_series"] = $row[1];
				$car_information[$counter]["model_number"] = $row[2];
				$car_information[$counter]["pictures_url"] = $row[3]."/1.jpg";
				$car_information[$counter]["grade"] = $row[4];
				$counter++;
			}
			$car_information["number"] = $counter;
			return $car_information;
		}

	// get car by price and grade
		public function get_car_by_grade_price($low_price, $high_price, $grade) {
			$query = "SELECT  brand.name, brand_series.name, model_number, pictures_url, grade FROM car JOIN brand USING (brand_id) JOIN brand_series USING (series_id) JOIN car_grade USING (car_grade_id) WHERE grade = '$grade' AND price_lowest >= $low_price AND price_highest <= $high_price";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			// counter of brand_series
			$counter = 0;
			while ($row = mysqli_fetch_row($result)) {
				$car_information[$counter]["brand"] = $row[0];
				$car_information[$counter]["brand_series"] = $row[1];
				$car_information[$counter]["model_number"] = $row[2];
				$car_information[$counter]["pictures_url"] = $row[3]."/1.jpg";
				$car_information[$counter]["grade"] = $row[4];
				$counter++;
			}
			$car_information["number"] = $counter;
			return $car_information;
		}		
	}
?>