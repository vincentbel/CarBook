<?php
	if (isset($_POST['tag']) && $_POST['tag']!=''){
		$tag = $_POST['tag'];

		require_once 'include/User_Setting.php';
		$setting = new User_Setting();

		// response array
		$response = array("tag" => $tag, "success" => 0, "error" => 0);
		if($tag == 'changePsd'){
			$username = $_POST['username'];
			$password = $_POST['password'];
			$result = $setting->change_password($username,$password);

			if($result){
				$response['success'] = 1;
				echo json_encode($response);
			}else{
				$response['error'] = 1;
				echo json_encode($response);
			}
		}
		if($tag == 'sex'){
			$username = $_POST['username'];
			$sex = $_POST['sex'];
			$result = $setting->update_avatar($username,$sex);

			if($result){
				$response['success'] = 1;
				echo json_encode($response);
			}else{
				$response['error'] = 1;
				echo json_encode($response);
			}
		}
	}
?>