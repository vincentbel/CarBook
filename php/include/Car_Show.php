<?php
	// 实现车辆展示功能

	class Car_Show {
		private $db;
		private $dbc;

		private $brand;
		private $series;
		private $model_number;

		// constructor
		function __construct($brand, $series, $model_number) {
			require_once 'DB_Connect.php';
			// connecting to database
			$this->db = new DB_Connect();
			$this->dbc = $this->db->connect();
			$this->brand = $brand;
			$this->series = $series;
			$this->model_number = $model_number;			
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

		public function getCarInformation() {
			$brand = $this->brand;
			$series = $this->series;
			$model_number = $this->model_number;
			$query = "SELECT * FROM car NATURAL JOIN brand JOIN brand_series USING (series_id) JOIN car_grade USING (car_grade_id) WHERE brand.name = '$brand' AND brand_series.name = '$series' AND model_number = '$model_number'";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			
			$result = mysqli_fetch_array($result);

			// test the result
			var_dump($result);
			return $result;
		}
	}

?>