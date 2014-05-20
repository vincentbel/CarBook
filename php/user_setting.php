<?php
	/**
	 * check for POST request
	 */
	if (isset($_POST['tag']) && $_POST['tag'] != '') {
		// get tag
		$tag = $_POST['tag'];

		// include database handler
		require_once 'include/User_Setting.php';
		
	}
?>