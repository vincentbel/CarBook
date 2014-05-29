<?php
	require_once 'include/Favour_car.php';

	$test = new Favour_car();
	$response = array("success" => 0,"error" => 0);
	$result = $test->get_car_rate();
	
	if($result){
		$response['success']=1;
		$response['1']=$result[0];
		$response['2']=$result[1];
		$response['3']=$result[2];
		$response['4']=$result[3];
		$response['5']=$result[4];
		$response['6']=$result[5];
		$response['7']=$result[6];
		$response['8']=$result[7];
		$response['9']=$result[8];
		$response['10']=$result[9];
		echo json_encode($response);

	}else{
		$response['error']=1;
		echo json_encode($response);
	}

?>