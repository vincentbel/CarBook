	CREATE TABLE car_engine
	(car_id  int(10) NOT NULL,
	 engine_model_number varchar(10) COMMENT '发动机型号',
	 emission_amount int(10) COMMENT '排量(ml)',
	 intake_form varchar(10) COMMENT '进气方式',
	 cylinder_number tinyint(2) COMMENT '气缸排列形式(L, V, W, H, R)',
	 cylinder_arrangement char(1) COMMENT '汽缸数(个)',
	 value_per_cylinder_number tinyint(2) COMMENT '每缸气门数(个)',
	 compression_ration single COMMENT '压缩比',
	 maximum_horsepower int(10) COMMENT '最大马力(Ps)',
	 maximum_power int(10) COMMENT '最大功率(Kw)',
	 maximum_power_speed int(10) COMMENT '最大功率转速(rpm)',
	 fuel_type varchar(10) COMMENT '燃料形式(汽油, 柴油)',
	 fuel_grade varchar(10) COMMENT '燃油标号(97号, 95号)',
	 environmental_level varchar(10) COMMENT '环保标准(国IV...)',
	 PRIMARY KEY (car_id) ,
	 FOREIGN KEY (car_id) REFERENCES car(car_id));

	CREATE TABLE color
	(color_id tinyint(2) NOT NULL AUTO_INCREMENT,
	 color varchar(10) NOT NULL,
	 PRIMARY KEY (color_id));

	CREATE TABLE car_color
	(car_color_id int(10) NOT NULL AUTO_INCREMENT,
	 car_id int(10) NOT NULL,
	 color_id tinyint(2) NOT NULL,
	 PRIMARY KEY (car_color_id),
	 FOREIGN KEY (car_id) REFERENCES car(car_id),
	 FOREIGN KEY (color_id) REFERENCES color(color_id));


	CREATE TABLE car_body_structure
	(car_id int(10) NOT NULL,
	 length int(10) COMMENT '长度(mm)',
	 width int(10) COMMENT '宽度(mm)',
	 height int(10) COMMENT '高度(mm)',
	 weight int(10) COMMENT '整备质量(kg)',
	 wheelbase int(10) COMMENT '轴距(mm)',
	 minimum_ground_clearance int(10) COMMENT '最小离地间隙(mm)',
	 door_number tinyint(2) COMMENT '车门数(个)',
	 seat_number tinyint(2) COMMENT '座位数(个)',
	 fuel_tank_capacity int(10) COMMENT '油箱容积(L)',
	 luggage_compartment_volume int(10), COMMENT '行李箱容积(L)'
	 PRIMARY KEY (car_id),
	 FOREIGN KEY (car_id) REFERENCES car(car_id));


	CREATE TABLE car_multimedia_configuration
	(car_id int(10) NOT NULL,
	 GPS boolean COMMENT 'GPS导航系统',
	 bluetooth boolean COMMENT '蓝牙',
	 car_phone boolean COMMENT '车载电话',
	 car_TV boolean COMMENT '车载电视',
	 rear_LCD_screen tinyint(2) COMMENT '后排液晶屏(个)',
	 external_audio_interface tinyint(2) COMMENT '外接音源接口(个)',
	 USB boolean COMMENT '外接音源接口',
	 AUX boolean COMMENT '外接音源接口',
	 iPod boolean COMMENT '外接音源接口',
	 Speaker_number tinyint(2) COMMENT '扬声器数量',
	 PRIMARY KEY (car_id),
	 FOREIGN KEY (car_id) REFERENCES car(car_id));


	CREATE TABLE car_tech_configuration
	(car_id int(10) NOT NULL,
	 automatic_parking boolean COMMENT '自动泊车入位',
	 night_version_system boolean COMMENT '夜视系统',
	 panoramic_camera boolean COMMENT '全景摄像头',
	 PRIMARY KEY (car_id),
	 FOREIGN KEY (car_id) REFERENCES car(car_id));