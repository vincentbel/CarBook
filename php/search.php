<?php
	// search car by keywords

	/**
	 * check for POST request
	 */	

	if (isset($_POST['tag']) && $_POST['tag'] != '') {
		// get tag
		$tag = $_POST['tag'];

		// include database handler
		require_once 'include/Car_Search.php';
		$db = new Car_Search();

		// response array
		$response = array("tag" => $tag, "success" => 0, "error" => 0);
		
		// check for tag type
		if ($tag == 'brand') {
			$brand = $db->get_car_brand();
			// close connection
			$db->close_dbc();

			if ($brand != false) {
				// brand found
				$response["success"] = 1;
				$response["brand_number"] = $brand["number"];

				for ($i = 0; $i < $brand["number"]; $i++) {
					$response["brand_".($i+1)] = $brand[$i];
					$response["brand_".($i+1)."_url"] = $brand[$i."_url"];
				}
				echo json_encode($response);
			} else {
				// brand not found
				$response["error"] = 1;
				$response["error_msg"] = "Brand not found!";
				echo json_encode($response);
			}
		} else if ($tag == 'brand_series') {
			$brand = $_POST['brand'];
			$brand_series = $db->get_brand_series($brand);
			// close connection
			$db->close_dbc();

			if ($brand_series != false) {
				// brand_series found
				$response["success"] = 1;
				$response["brand_series_number"] = $brand_series["number"];

				for ($i = 0; $i < $brand_series["number"]; $i++) {
					$response["brand_series_".($i+1)] = $brand_series[$i];
				}
				echo json_encode($response);
			} else {
				// brand_series not found
				$response["error"] = 1;
				$response["error_msg"] = "Brand series not found!";
				echo json_encode($response);
			}
		} else if ($tag == 'model_number') {
			$brand = $_POST['brand'];
			$brand_series = $_POST['brand_series'];
			$model_number = $db->get_model_number($brand_series);
			// close connection
			$db->close_dbc();

			if ($model_number != false) {
				// model_number found
				$response["success"] = 1;
				$response["model_number_amount"] = $model_number["number"];

				for ($i = 0; $i < $model_number["number"]; $i++) {
					$response["model_number_".($i+1)] = $model_number[$i];
				}
				echo json_encode($response);
			} else {
				// model_number not found
				$response["error"] = 1;
				$response["error_msg"] = "Model_number not found!";
				echo json_encode($response);
			}
		} else if ($tag = 'conditional_search') {
			$low_price = $_POST['low_price'];
			$high_price = $_POST['high_price'];
			$grade = $_POST['grade'];
			if ($low_price != '' && $high_price != '' && $grade == '') {
				// the car search by prices
				$car = $db->get_car_between_prices($low_price, $high_price);
				// close connection
				$db->close_dbc();
				if ($car["number"] > 0) {
					// car found
					$response["success"] = 1;
					$response["search_number"] = $car["number"];
				
					for ($i = 0; $i < $car["number"]; $i++) {
						$response["car_".($i+1)] = $car[$i];
					}
					echo json_encode($response);
				} else {
					// car not found
					$response["error"] = 1;
					$response["error_msg"] = "Car not found!";
					echo json_encode($response);
				}
			} else if ($low_price == '' && $high_price == '' && $grade != '') {
				// the car search by prices
				$car = $db->get_car_by_grade($grade);
				// close connection
				$db->close_dbc();
				if ($car["number"] > 0) {
					// car found
					$response["success"] = 1;
					$response["search_number"] = $car["number"];
				
					for ($i = 0; $i < $car["number"]; $i++) {
						$response["car_".($i+1)] = $car[$i];
					}
					echo json_encode($response);
				} else {
					// car not found
					$response["error"] = 1;
					$response["error_msg"] = "Car not found!";
					echo json_encode($response);
				}				
			} else if ($low_price != '' && $high_price != '' && $grade != '') {
				// the car search by prices and grade
				$car = $db->get_car_by_grade_price($low_price, $high_price, $grade);
				// close connection
				$db->close_dbc();
				if ($car["number"] > 0) {
					// car found
					$response["success"] = 1;
					$response["search_number"] = $car["number"];
				
					for ($i = 0; $i < $car["number"]; $i++) {
						$response["car_".($i+1)] = $car[$i];
					}
					echo json_encode($response);
				} else {
					// car not found
					$response["error"] = 1;
					$response["error_msg"] = "Car not found!";
					echo json_encode($response);
				}				
			}
		}
	}
?>