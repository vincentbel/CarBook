<?php
	// show the information of a specific car

	/**
	 * File to handle all API requests
	 * Accepts GET and POST
	 * 
	 * Each request will be identified by TAG
	 * Response will be JSON data
	 */

	 /**
	  * check for POST request
	  */
	if (isset($_POST['tag']) && $_POST['tag']!='') {
		// get tag
		$tag = $_POST['tag'];

		// include database handler
		require_once 'include/Car_Show.php';
		$brand = $_POST['brand'];
		$series = $_POST['series'];
		$model_number = $_POST['model_number'];
		$db = new Car_Show($brand, $series, $model_number);

		// response array
		$response = array("tag" => $tag, "success" => 0, "error" => 0);
		
		// check for tag type
		if ($tag == 'showcar') {
			$car = $db->getCompositeInformation();
			// close connection
			$db->close_dbc();

			if ($car != false) {
				// car found
				$response["success"] = 1;
				$response["car_grade"] = $car["car_grade"];
				$response["car_body_structure"] = $car["car_body_structure"];
				$response["price"] = $car["price"];
				$response["transmission"] = $car["transmission"];
				$response["sale_company_num"] = $car["sale_company_num"];
				for ($i = 0; $i < $car["sale_company_num"]; $i++) {
					$response["sale_company_".($i+1)] = $car["sale_company_".($i+1)];
				}
				echo json_encode($response);
			} else {
				// car not found
				$response["error"] = 1;
				$response["error_msg"] = "Car not found!";
				echo json_encode($response);
			}
		} else {
			echo "Invaild request!";
		}
	}
?>