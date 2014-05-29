<?php
require_once ('include/User_Comments.php');
	$user_comments = new User_Comments();
	if (get_tag('tag') == 'my_comments') {
		$user_id = get_tag('user_id');
		if (!is_numeric($user_id)) {
			$respose = array(
				'success' => 0, 
				'error_message' => 'user_id is not a number'
				);
			echo json_encode($respose);
		}
		$respose = array('success' => 1);
		$comments = $user_comments->get_my_comments($user_id);
		$respose['comments'] = $comments;
		echo json_encode($respose,JSON_FORCE_OBJECT);
	}




	function get_tag($tag) {
		if (isset($_POST[$tag])) {
			return $_POST[$tag];
		} else {
			echo "Invalid request!";
			exit();
		}
	}
?>