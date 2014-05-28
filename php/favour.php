<?php
	if (isset($_POST['tag']) && $_POST['tag']!=''){
		$tag = $_POST['tag'];
	}

	require_once 'include/Favour_Car.php';
	$favour = new Favour_Car();

	$response = array("tag" => $tag,"success" => 0,"error" =>0);
	if ($tag == 'favour'){
		$result = $favour->get_car_rate();
	
	if($result){
			$response['success']=1;
			for ($i = 0; $i < count($result); $i++) {
				$response[$i + 1] = $result[$i];
			}
			echo json_encode($response);
		}else{
			$response['error']=1;
			echo json_encode($response);
		}	
	}
?>