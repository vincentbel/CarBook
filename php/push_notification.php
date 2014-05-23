<?php
	if (isset($_POST['tag']) && $_POST['tag'] != '') {
	// get tag
		$tag = $_POST['tag'];
		require_once 'include/DB_Connect.php';
		require_once 'include/Car_Search.php';
		$db = new DB_Connect();
		$dbc = $db->connect();

		// response array
		$response = array("tag" => $tag, "success" => 0, "error" => 0);
	
		// check for tag type
		if ($tag == 'push_notification') {
			$user_id = $_POST['user_id'];
			$query = "SELECT favour_brand_id, grade, affordable_price_lowest, affordable_price_highest FROM user_favour_type WHERE user_id = '$user_id'";
			$result = mysqli_query($dbc, $query);
			$result = mysqli_fetch_array($result);
			$favour_brand_id = $result["favour_brand_id"];
			$grade = $result["grade"];
			$price_lowest = $result["affordable_price_lowest"];
			$price_highest = $result["affordable_price_highest"];
			$car = new Car_Search();
			$car_information;
			if ($grade != '' && $price_highest != '' && $price_lowest != '') {
				$car_information = $car->get_car_by_grade_price($price_lowest, $price_highest, $grade);
			} else if ($grade == '' && $price_highest != '' && $price_lowest != '') {
				$car_information = $car->get_car_between_prices($price_lowest, $price_highest);
			} else if ($grade != '' && $price_lowest == '' && $price_highest == '') {
				$car_information = $car->get_car_by_grade($grade);
			} else {
				$car_information["number"] = 0;
			}

			if ($car_information["number"] > 0) {
					// car found
					$response["success"] = 1;
					$response["search_number"] = $car_information["number"];
				
					for ($i = 0; $i < $car_information["number"]; $i++) {
						$response["car_".($i+1)] = $car_information[$i];
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
?>