<?php
	/**
	 * check for POST request
	 */
	if (isset($_POST['tag']) && $_POST['tag'] != '') {
		// get tag
		$tag = $_POST['tag'];

		// include database handler
		require_once 'include/User_Setting.php';
		$user = new User_Setting();

		if ($tag == 'update_information') {
			
		} else if ($tag == 'update_avatar') {
			$user_id = $_POST['user_id'];
			$status = $_POST['status'];

			$result = $user->update_avatar($user_id, $status);
			if ($result) {
				return true;
			} else {
				return false;
			}
		}
	}
?>