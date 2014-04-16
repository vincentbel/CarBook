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
		if ($tag == 'query') {
			$car = $db->getCarInformation();
			if ($car != false){
				// car found
				$response["success"] = 1;
				$response["grade"] = $car["grade"];
				$response["price_lowest"] = $car["price_lowest"];
				$response["price_highest"] = $car["price_highest"];
				$response["name"] = $car["name"];
				echo json_encode($response);				
			} else {
				// car not found
				$response["error"] = 1;
				$response["error_msg"] = "Car not found!";
				echo json_encode($response);
			}

		}
	} else {
		echo "Invaild request!";
	}
?>