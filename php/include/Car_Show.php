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

	/**
	 *	function for test
	 */
		
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

	/**
	 * get car_id by brand, series and model_number
	 */
		public function getCarId() {
			$brand = $this->brand;
			$series = $this->series;
			$model_number = $this->model_number;
			$query = "SELECT car_id FROM car NATURAL JOIN brand JOIN brand_series USING (series_id) JOIN car_grade USING (car_grade_id) WHERE brand.name = '$brand' AND brand_series.name = '$series' AND model_number = '$model_number'";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			$result = mysqli_fetch_array($result);

			return $result["car_id"];
		}

	/**
	 * get produce_company_id by car_id
	 */
		public function getProduceCompanyId() {
			$car_id = $this->getCarId();
			$query = "SELECT company_id FROM car WHERE car_id = '$car_id'";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			$result = mysqli_fetch_array($result);

			return $result["company_id"];
		}

	/**
	 * get sale_company_id by car_id
	 */
		public function getSaleCompanyId() {
			$car_id = $this->getCarId();
			$query = "SELECT sale_company_id FROM sale WHERE car_id = '$car_id'";
			$result = mysqli_query($this->dbc, $query);
			// check the query result
			$this->check_sql_error($this->dbc, $query, $result);
			// count the number of sale_company
			$counter = 0;
			while ($row = mysqli_fetch_row($result)) {
				$sale_company_id[$counter++] = $row[0];
			}
			return $sale_company_id;
		}
	
	/**
	 * getCompositeInformation
	 */
		public function getCompositeInformation() {
			$comInformation;
			$car_id = $this->getCarId();
			// query car's grade
			$query = "SELECT grade FROM car NATURAL JOIN car_grade WHERE car.car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$this->check_sql_error($this->dbc, $query, $result);
			$comInformation["car_grade"] = $result["grade"];
			
			// query car's body structure
			$query = "SELECT door_number, seat_number FROM car NATURAL JOIN car_body_structure WHERE car.car_id = $car_id ";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$comInformation["car_body_structure"] = $result["door_number"]."门".$result["seat_number"]."座";			

			// query car's price
			$query = "SELECT price_highest, price_lowest, transmission FROM car WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$comInformation["price"] = ($result["price_lowest"]/10000)."万-".($result["price_highest"]/10000)."万";
			$comInformation["transmission"] = $result["transmission"];

			// query car's sale_company
			$sale_company_id = $this->getSaleCompanyId();
			$comInformation["sale_company_num"] = count($sale_company_id);
			// mark different sale_company
			$counter = 1;
			foreach ($sale_company_id as $value) {
				$query = "SELECT name, telephone, address FROM sale_company WHERE sale_company_id = $value";
				$result = mysqli_query($this->dbc, $query);
				$result = mysqli_fetch_array($result);
				$comInformation["sale_company_".$counter]["name"] = $result["name"];
				$comInformation["sale_company_".$counter]["telephone"] = $result["telephone"];
				$comInformation["sale_company_".$counter]["address"] = $result["address"];
				$counter++;
			}
			var_dump($comInformation);
			return $comInformation;
		}
	}

?>