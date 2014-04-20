<?php

	require_once 'include/User_Setting.php';
	/*$username="Aleeex";
	$email='js@local.com';
	$password='jing';
	$gender=1;
	$birthdate = '1994-03-25';
	*/

	$test = new User_Setting();

	// response _POST
	$response = array("success" => 0, "error" => 0);
	
	$user = $test->update_infomation('Aleeex','jss@local.com','jing',1,'1994-03-25');
	if($user){
		
		$response['success']=1;
		$response['username']=$user['username'];
		$response['updated_at']=$user['updated_at'];
		echo json_encode($response);
	}else{
		$response['error']=1;
		echo json_encode($response);
	}

	$favour = $test->update_user_favour_type('Aleeex',3,'great','hhxxx',30,50);
	if($favour){
		$response['success']=1;;
		echo json_encode($response);
	}else{
		$response['error']=1;
		echo json_encode($response);
	}
?>