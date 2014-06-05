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
	} elseif (get_tag('tag') == 'add_comments') {
		$username = get_tag('username');
		/*if (!is_numeric($user_id)) {
			$respose = array(
				'success' => 0, 
				'error_message' => 'user_id is not a number'
				);
			echo json_encode($respose);
		}*/
		$car_id = get_tag('car_id');
		$comment = get_tag('comment');
		date_default_timezone_set('Asia/Shanghai');
		$comment_time = date("Y-m-d H:i:s");
		$rate = get_tag('rate');
		$user_comments->add_comment($username, $car_id, $comment, $comment_time, $rate);
		$respose = array(
				'success' => 1
				);
			echo json_encode($respose);
	} elseif (get_tag('tag') == 'delete_comments') {
		$username = get_tag('username');
		$car_id = get_tag('car_id');
		$comment = get_tag('comment');
		$result = $user_comments->delete_comment($username, $car_id, $comment);
		if ($result == 0) {
			$respose = array(
				'success' => 0, 
				'error_message' => 'no comment deleted'
				);
			echo json_encode($respose);
		} else {
			$respose = array(
					'success' => 1
					);
				echo json_encode($respose);
		}
	} elseif (get_tag('tag') == 'get_car_comments') {
		$car_id = get_tag('car_id');
		$respose = array('success' => 1);
		$comments = $user_comments->get_all_comments($car_id);
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