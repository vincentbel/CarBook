<?php
	// compare the information of two specific cars

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
	 if (isset($_POST['tag']) && $_POST['tag'] != '') {
	 	// get tag
	 	$tag = $_POST['tag'];

	 	// include database handler
	 	require_once 'include/Car_show.php';
	 	$brand_1 = $_POST['brand_1'];
	 	$series_1 = $_POST['series_1'];
	 	$model_number_1 = $_POST['model_number_1'];
	 	$brand_2 = $_POST['brand_2'];
	 	$series_2 = $_POST['series_2'];
	 	$model_number_2 = $_POST['model_number_2'];

	 	$db_1 = new Car_show($brand_1, $series_1, $model_number_1);
	 	$db_2 = new Car_show($brand_2, $series_2, $model_number_2);

	 	// response array
	 	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	 	// check for tag type
	 	if ($tag == 'compare') {
	 		$car_1 = $db_1->get_car_configuration();
	 		$car_2 = $db_2->get_car_configuration();
	 		// close connection
	 		$db_1->close_dbc();
	 		$db_2->close_dbc();

	 		if ($car_1 != false && $car_2 != false) {
	 			// car found 
	 			$response["success"] = 1;
	 			$response["car_1"] = $car_1;
	 			$response["car_2"] = $car_2;
	 			echo json_encode($response);
	 		} else {
	 			// car not found
	 			$response["error"] = 1;
	 			$response["error_msg"] = "Car not found!";
	 			echo json_encode($response);
	 		}
	 	}	 	
	 }
?>