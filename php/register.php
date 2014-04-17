<?php
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
		require_once 'include/DB_Functions.php';
		$db = new DB_Functions();

		// response _POST
		$response = array("tag" => $tag, "success" => 0, "error" => 0);

		if ($tag == 'register') {
			// Request type is Register new user
			$username = $_POST['username'];
			$email = $_POST['email'];
			$password = $_POST['password'];

			// check if user is already existed
			if ($db->isUserExistedByUsername($username)) {
				// user is already existed - error response
				$response["error"] = 2;
				$response["error_msg"] = "User already existed!";
				echo json_encode($response);
			} else {
				// store user
				$user = $db->storeUser($username, $email, $password);
				// close connection
				$db->close_dbc();
				if ($user) {
					// user stored successfully
					$response["success"] = 1;
					$response["username"] = $user["username"];
					$response["email"] = $user["email"];
					$response["created_at"] = $user["created_at"];
					echo json_encode($response);
				} else {
					// user failed to store
					$response["error"] = 1;
					$response["error_msg"] = "Error occured in Registartion";
					echo json_encode($response);
				}
			}
		}
	} else {
		echo "Invaild Request!";
	}

	//todo 邮箱唯一性验证
?>
