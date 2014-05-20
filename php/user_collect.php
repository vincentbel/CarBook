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
		} else if ($_POST['tag'] == 'show_collect') {
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
		}
	}
?>