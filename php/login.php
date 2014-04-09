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
		$response = _POST("tag" => $tag, "success" => 0, "error" => 0);

		// check for tag type
		if ($tag == 'login') {
			// Require type is check login
			$username = $_POST['username'];
			$password = $_POST['password'];

			// check for user
			$user = $db->getUserByUsernameAndPassword($username, $password);
			if ($user != false) {
				// user found
				// echo json with success = 1
				$response["success"] = 1;
				$response["username"] = $user["username"];
				$response["email"] = $user["email"];
				$response["created_at"] = $user["created_at"];
				echo json_encode($response);
			} else {
				// user not found
				// echo json with error = 1
				$response["error"] = 1;
				$response["error_msg"] = "Incorrect email or password!";
				echo json_encode($response);
			}
		} 
	} else {
		echo "Invaild Request!";
	}
?>