<?php
	
	require_once 'include/User_Collect.php';

	$test = new User_Collect();
	$response = array("success" => 0, "error" => 0);
	$collect = $test->delete_collected_cars(1, 12);
	if($collect){
		$response['success']=1;
		$response['collect_time']=$collect['collect_time'];
		echo json_encode($response);
	}else{
		$response['error']=1;
		echo json_encode($response);
	}
?>