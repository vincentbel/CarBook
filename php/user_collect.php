<?php
	/**
	 * check for POST request
	 */
	if (isset($_POST['tag']) && $_POST['tag'] != '') {
		// get tag
		$tag = $_POST['tag'];

		// include database handler
		require_once 'include/User_Collect.php';


		$uc = new User_Collect();
		$response = array("success" => 0, "error" => 0);

		if ($_POST['tag'] == 'collect') {
			$user_id = $_POST['user_id'];
			$car_id = $_POST['car_id'];
			$result = $uc->collect_cars($user_id, $car_id);

			// close connection
			$uc->close_dbc();
			if ($result) {
				$response["success"] = 1;
				$response['collect_time']=$result['collect_time'];
				echo json_encode($response);
			} else {
				$response["error"] = 1;
				$response["error_msg"] = "Collect failed!";
				echo json_encode($response);
			}
		} else if ($_POST['tag'] == 'delete_collect') {
			$user_id = $_POST['user_id'];
			$car_id = $_POST['car_id'];
			$result = $uc->delete_collected_cars($user_id, $car_id);

			// close connection
			$uc->close_dbc();
			if ($result) {
				$response["success"] = 1;
				echo json_encode($response);
			} else {
				$response["error"] = 1;
				$response["error_msg"] = "Delete failed!";
				echo json_encode($response);
			}
		} else if ($_POST['tag'] == 'my_collection') {
			$user_id = $_POST['user_id'];
			$result = $uc->show_collected_cars($user_id);

			// close connection
			$uc->close_dbc();
			if ($result) {
				$response["success"] = 1;
				$number = $result["number"];
				$response["number"] = $number;
				for ($i = 0; $i < $number; $i++) {
					$response["car_".($i + 1)] = $result[$i];
				}
				echo json_encode($response);
			} else {
				$response["error"] = 1;
				$response["error_msg"] = "Show Collect failed!";
				echo json_encode($response);
			}
		} else if ($_POST['tag'] == 'default_collection') {
			$car_id = json_decode($_POST['car_id']);
			$counter = 0;
			foreach ($car_id as $key => $value) {
				$car_id = $value->car_id;
				$car = $uc->get_information_by_car_id($car_id);
				if (!$car) {
					break;
				}
				$response["car_".($counter + 1)] = $car;
				$counter++;
			}
			if ($counter > 0) {
				$response["success"] = 1;
				$response["number"] = $counter;
				echo json_encode($response);
			} else {
				$response["error"] = 1;
				$response["error_msg"] = "Show collections failed!";
				echo json_encode($response);
			}
		} else if ($_POST['tag'] == 'collect_sync') {
			$result = json_decode($_POST['collect_data']);
			$ans = true; // check the sync status
			$user_id = $_POST['user_id'];
			foreach ($result as $key => $value) {
				//$user_id = $value->user_id;
				$car_id = $value->car_id;
				$ans = $uc->collect_cars($user_id, $car_id);
				if (!$ans) {
					break;
				}
			}
			if ($ans) {
				$response["success"] = 1;
				$response["cars"] = $uc->get_cars_collect_time($user_id);
				$uc->close_dbc();
				echo json_encode($response);
			} else {
				$response["error"] = 1;
				$response["error_msg"] = "Sync failed!";
				$uc->close_dbc();
				echo json_encode($response);
			}
		}
	}
?>