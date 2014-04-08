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

		// response Array
		$response = array("tag" => $tag, "success" => 0, "error" => 0);

		if ($tag == 'register') {
			// Request type is Register new user
			$name = $_POST['name'];
			$email = $_POST['email'];
			$password = $_POST['password'];

			// check if user is already existed
			if ($db->isUserExisted($email)) {
				// user is already existed - error response
				$response["error"] = 2;
				$response["error_msg"] = "User already existed!";
				echo json_encode($response);
			} else {
				// store user
				$user = $db->storeUser($name, $email, $password);
				if ($user) {
					// user stored successfully
					$response["success"] = 1;
					$response["user"]["name"] = $user["user_name"];
					$response["user"]["email"] = $user["email"];
					$response["user"] ["created_at"] = $user["created_at"];
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
?>