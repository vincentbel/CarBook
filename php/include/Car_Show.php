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

			// 修改汽车的浏览次数，每浏览一次，浏览次数增加一
			$car_id = $this->get_car_id();
			$query = "UPDATE car SET view_times = view_times + 1 WHERE car_id = $car_id";
			mysqli_query($this->dbc, $query);	
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
	 * get car_id by brand, series and model_number
	 */
		public function get_car_id() {
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
		public function get_produce_company_id() {
			$car_id = $this->get_car_id();
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
		public function get_sale_company_id() {
			$car_id = $this->get_car_id();
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
		public function get_composite_information() {
			$com_information;
			$car_id = $this->get_car_id();
			// query car's grade
			$query = "SELECT car_id, grade FROM car NATURAL JOIN car_grade WHERE car.car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$this->check_sql_error($this->dbc, $query, $result);
			$com_information["car_grade"] = $result["grade"];
			$com_information["car_id"] = $result["car_id"];
			
			// query car's body structure
			$query = "SELECT door_number, seat_number FROM car NATURAL JOIN car_body_structure WHERE car.car_id = $car_id ";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$com_information["car_body_structure"] = $result["door_number"]."门".$result["seat_number"]."座";			

			// query car's price
			$query = "SELECT price_highest, price_lowest, transmission FROM car WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$com_information["price"] = ($result["price_lowest"]/10000)."万-".($result["price_highest"]/10000)."万";
			$com_information["transmission"] = $result["transmission"];

			// query car's sale_company
			$sale_company_id = $this->get_sale_company_id();
			$com_information["sale_company_num"] = count($sale_company_id);
			// mark different sale_company
			$counter = 1;
			foreach ($sale_company_id as $value) {
				$query = "SELECT name, telephone, address FROM sale_company WHERE sale_company_id = $value";
				$result = mysqli_query($this->dbc, $query);
				$result = mysqli_fetch_array($result);
				$com_information["sale_company_".$counter]["name"] = $result["name"];
				$com_information["sale_company_".$counter]["telephone"] = $result["telephone"];
				$com_information["sale_company_".$counter]["address"] = $result["address"];
				$counter++;
			}

			$query = "SELECT pictures_url FROM car WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$url = $result["pictures_url"]."/1.jpg";
			$com_information["pictures_url"] = $url;
			//var_dump($com_information);
			return $com_information;
		}

	/**
	 * get car's details information
	 */
		public function get_car_configuration() {
			$car_configuration;
			$car_id = $this->get_car_id();

			// get car's engine information
			$query = "SELECT * FROM car_engine WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$car_configuration["car_engine"]["engine_model_number"] = $result["engine_model_number"];
			$car_configuration["car_engine"]["emission_amount"] = $result["emission_amount"];
			$car_configuration["car_engine"]["intake_form"] = $result["intake_form"];
			$car_configuration["car_engine"]["cylinder_number"] = $result["cylinder_number"];
			$car_configuration["car_engine"]["cylinder_arrangement"] = $result["cylinder_arrangement"];
			$car_configuration["car_engine"]["value_per_cylinder_number"] = $result["value_per_cylinder_number"];
			$car_configuration["car_engine"]["compression_ration"] = $result["compression_ration"];
			$car_configuration["car_engine"]["maximum_horsepower"] = $result["maximum_horsepower"];
			$car_configuration["car_engine"]["maximum_power"] = $result["maximum_power"];
			$car_configuration["car_engine"]["maximum_power_speed"] = $result["maximum_power_speed"];
			$car_configuration["car_engine"]["fuel_type"] = $result["fuel_type"];
			$car_configuration["car_engine"]["fuel_grade"] = $result["fuel_grade"];
			$car_configuration["car_engine"]["environmental_level"] = $result["environmental_level"];

			// get car's body structure
			$query = "SELECT * FROM car_body_structure WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$car_configuration["car_body_structure"]["length"] = $result["length"];
			$car_configuration["car_body_structure"]["width"] = $result["width"];
			$car_configuration["car_body_structure"]["height"] = $result["height"];
			$car_configuration["car_body_structure"]["weight"] = $result["weight"];
			$car_configuration["car_body_structure"]["wheelbase"] = $result["wheelbase"];
			$car_configuration["car_body_structure"]["minimum_ground_clearance"] = $result["minimum_ground_clearance"];
			$car_configuration["car_body_structure"]["door_number"] = $result["door_number"];
			$car_configuration["car_body_structure"]["seat_number"] = $result["seat_number"];
			$car_configuration["car_body_structure"]["fuel_tank_capacity"] = $result["fuel_tank_capacity"];
			$car_configuration["car_body_structure"]["luggage_compartment_volume"] = $result["luggage_compartment_volume"];
		
			// get car's multimedia_configuration
			$query = "SELECT * FROM car_multimedia_configuration WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$car_configuration["car_multimedia"]["GPS"] = $result["GPS"];
			$car_configuration["car_multimedia"]["bluetooth"] = $result["bluetooth"];
			$car_configuration["car_multimedia"]["car_phone"] = $result["car_phone"];
			$car_configuration["car_multimedia"]["car_TV"] = $result["car_TV"];
			$car_configuration["car_multimedia"]["rear_LCD_screen"] = $result["rear_LCD_screen"];
			$car_configuration["car_multimedia"]["external_audio_interface"] = $result["external_audio_interface"];
			$car_configuration["car_multimedia"]["USB"] = $result["USB"];
			$car_configuration["car_multimedia"]["AUX"] = $result["AUX"];
			$car_configuration["car_multimedia"]["iPod"] = $result["iPod"];
			$car_configuration["car_multimedia"]["Speaker_number"] = $result["Speaker_number"];			
		
			// get car's technology configuration
			$query = "SELECT * FROM car_tech_configuration WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$car_configuration["car_hightech"]["automatic_parking"] = $result["automatic_parking"];
			$car_configuration["car_hightech"]["night_version_system"] = $result["night_version_system"];
			$car_configuration["car_hightech"]["panoramic_camera"] = $result["panoramic_camera"];

			//var_dump($car_configuration);
			return $car_configuration;
		}

	/**
	 * get sale_company
	 */
		public function get_sale_company_information() {
			$sale_company_information;
			$sale_company_id = $this->get_sale_company_id();
			$sale_company_information["sale_company_num"] = count($sale_company_id);
			// mark different sale_company
			$counter = 1;
			foreach ($sale_company_id as $value) {
				$query = "SELECT name, telephone, address, price_lowest, price_highest FROM sale_company NATURAL JOIN sale WHERE sale_company_id = $value";
				$result = mysqli_query($this->dbc, $query);
				$result = mysqli_fetch_array($result);
				$sale_company_information["sale_company_".$counter]["name"] = $result["name"];
				$sale_company_information["sale_company_".$counter]["telephone"] = $result["telephone"];
				$sale_company_information["sale_company_".$counter]["address"] = $result["address"];
				$sale_company_information["sale_company_".$counter]["price_lowest"] = $result["price_lowest"];
				$sale_company_information["sale_company_".$counter]["price_highest"] = $result["price_highest"];
				$counter++;
			}
			//var_dump($sale_company_information);
			return $sale_company_information;
		}

	/**
	 * get picture url
	 */
		public function get_pictures_url() {
			$car_id = $this->get_car_id();
			$query = "SELECT pictures_url, pictures_num FROM car WHERE car_id = $car_id";
			$result = mysqli_query($this->dbc, $query);
			$result = mysqli_fetch_array($result);
			$url = $result["pictures_url"];
			$pictures_num = $result["pictures_num"];

			$pictures_url;
			$pictures_url["pictures_num"] = $pictures_num;
			for ($i = 1; $i <= $pictures_num; $i++) {
				$pictures_url[$i] = $url."/$i".".jpg";
			}
			//var_dump($pictures_url);
							
			return $pictures_url;
		}

	}
?>