<?php
	// show the information of a specific car

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
	if (isset($_POST['tag']) && $_POST['tag']!='') {
		// get tag
		$tag = $_POST['tag'];

		// include database handler
		require_once 'include/Car_Show.php';
		$brand = $_POST['brand'];
		$series = $_POST['series'];
		$model_number = $_POST['model_number'];
		$db = new Car_Show($brand, $series, $model_number);

		// response array
		$response = array("tag" => $tag, "success" => 0, "error" => 0);
		
		// check for tag type
		if ($tag == 'showcar') {
			$car = $db->get_composite_information();
			// close connection
			$db->close_dbc();

			if ($car != false) {
				// car found
				$response["success"] = 1;
				$response["car_grade"] = $car["car_grade"];
				$response["car_body_structure"] = $car["car_body_structure"];
				$response["price"] = $car["price"];
				$response["transmission"] = $car["transmission"];
				$response["sale_company_num"] = $car["sale_company_num"];
				for ($i = 0; $i < $car["sale_company_num"]; $i++) {
					$response["sale_company_".($i+1)] = $car["sale_company_".($i+1)];
				}
				echo json_encode($response);
			} else {
				// car not found
				$response["error"] = 1;
				$response["error_msg"] = "Car not found!";
				echo json_encode($response);
			}
		} elseif ($tag == 'configuration') {
			$car = $db->get_car_configuration();
			// close connection
			$db->close_dbc();

			if ($car != false) {
				// car found
				$response["success"] = 1;
				$response["car_engine"] = $car["car_engine"];
				$response["car_body_structure"] = $car["car_body_structure"];
				$response["car_multimedia"] = $car["car_multimedia"];
				$response["car_hightech"] = $car["car_hightech"];
				echo json_encode($response);
			} else {
				// car not found
				$response["error"] = 1;
				$response["error_msg"] = "Car not found!";
				echo json_encode($response);
			}
		} elseif ($tag == 'sale_companys') {
			$sale_companys = $db->get_sale_company_information();
			// close connection
			$db->close_dbc();

			if ($sale_companys != false) {
				// companys found
				$response["success"] = 1;
				$response["sale_company_num"] = $sale_companys["sale_company_num"];
				for ($i = 0; $i < $sale_companys["sale_company_num"]; $i++) {
					$response["sale_company_".($i+1)] = $sale_companys["sale_company_".($i+1)];
				}
				echo json_encode($response);				
			} else {
				// companys not found
				$response["error"] = 1;
				$response["error_msg"] = "Sale Companys not found!";
				echo json_encode($response);
			}
		} elseif ($tag == 'show_pictures') {
			$pictures_url = $db->get_pictures_url();
			// close connection
			$db->close_dbc();

			if ($pictures_url != false) {
				// pictures found
				$response["success"] = 1;
				$response["pictures_num"] = $pictures_url["pictures_num"];
				for ($i=1; $i <= $pictures_url["pictures_num"]; $i++) { 
					$response["pictures_".$i."_url"] = $pictures_url[$i];
				}
				echo json_encode($response);
			} else {
				// pictures not found
				$response["error"] = 1;
				$response["error_msg"] = "Pictures not found!";
				echo json_encode($response);
			}
		} 
	} else {
			echo "Invaild request!";
	}
?>