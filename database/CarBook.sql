    CREATE TABLE user
     (user_id	int(10) NOT NULL AUTO_INCREMENT,
     username    varchar(32) NOT NULL COMMENT '用户名',
     encrypted_password    char(32) NOT NULL COMMENT '加密后的密码',
     gender      tinyint(1) DEFAULT '0' COMMENT '性别',
     birthdate	date,
     avatar_status		tinyint(1) DEFAULT '0' COMMENT '用户是否有自定义头像',
     email varchar(50) COMMENT '用户邮箱',
     salt char(10) NOT NULL COMMENT '对密码进行加密',
     created_at	datetime NOT NULL COMMENT '用户注册时间',
     updated_at datetime NOT NULL COMMENT '用户最后更新时间',
     PRIMARY KEY (user_id));

    CREATE TABLE car
 	  (car_id		int(10)	NOT NULL AUTO_INCREMENT COMMENT '汽车的唯一标识',
 	   car_grade_id	tinyint(2) NOT NULL COMMENT '汽车级别id',
  	 price_highest int(12) NOT NULL COMMENT '最高价格',
  	 price_lowest int(12) NOT NULL COMMENT '最低价格',
  	 brand_id int(6) NOT NULL COMMENT '品牌id',
  	 series_id int(6) NOT NULL COMMENT '车系id',
  	 model_number varchar(32) NOT NULL COMMENT '汽车型号',
  	 view_times int(6) NOT NULL COMMENT '观看次数',
  	 time_to_market date NOT NULL COMMENT '上市时间',
  	 company_id int(6) NOT NULL COMMENT '生产厂商id',
  	 PRIMARY KEY (car_id),
  	 FOREIGN KEY (company_id) REFERENCES produce_company(company_id)
  	 FOREIGN KEY (car_grade_id) REFERENCES car_grade(car_grade_id)
  	 FOREIGN KEY (brand_id) REFERENCES brand(brand_id)
  	 FOREIGN KEY (series_id) REFERENCES brand_series(series_id));

    CREATE TABLE produce_company
  	(company_id int(6) NOT NULL AUTO_INCREMENT COMMENT '厂商id',
  	 name varchar(20) NOT NULL COMMENT '厂商名称',
  	 set_up_time int(11) NOT NULL COMMENT '厂商建立时间',
  	 nationality varchar(20) NOT NULL COMMENT '国别',
  	 PRIMARY KEY (company_id));

    CREATE TABLE car_grade
  	(car_grade_id	tinyint(2)	NOT NULL,
  	 grade  varchar(10)	NOT NULL,
  	 PRIMARY KEY (car_grade_id));

    CREATE TABLE brand
  	(brand_id int(6) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
  	 name	varchar(20) NOT NULL COMMENT '品牌名称',
	   PRIMARY KEY (brand_id));

    CREATE TABLE brand_series 
    (series_id int(6) NOT NULL AUTO_INCREMENT COMMENT '系列id',
     brand_id int(6) NOT NULL COMMENT '品牌id',
     name varchar(20) NOT NULL COMMENT '系列名称',
     PRIMARY KEY (series_id),
     FOREIGN KEY (brand_id) REFERENCES brand(brand_id));

    CREATE TABLE sale_company
  	(sale_company_id int(6) NOT NULL AUTO_INCREMENT COMMENT '销售公司id',
  	 name varchar(20) NOT NULL COMMENT '销售公司名称',
  	 telephone varchar(20) NOT NULL COMMENT '联系电话',
  	 address varchar(64) NOT NULL COMMENT '地址',
  	 PRIMARY KEY (sale_company_id));

    CREATE TABLE sale
  	(sale_id int(10) NOT NULL AUTO_INCREMENT,
  	 car_id int(10) NOT NULL,
  	 sale_company_id int(6) NOT NULL,
  	 sale_date date NOT NULL,
  	 PRIMARY KEY (sale_id),
  	 FOREIGN KEY (car_id) REFERENCES car(car_id),
  	 FOREIGN KEY (sale_company_id) REFERENCES sale_company(sale_company_id));

    CREATE TABLE user_favour_type
 	  (user_id	int(10)	NOT NULL,
   	 favour_brand_id	int(6),
   	 grade	varchar(16)	COMMENT '汽车级别',
   	 body_structure	varchar(16) COMMENT '车身结构',
   	 affordable_price_lowest	int(12),
   	 affordable_price_highest	int(12)	COMMENT '可承受价格范围',
   	 PRIMARY KEY (user_id),
   	 FOREIGN KEY (favour_brand_id) REFERENCES brand(brand_id));

    CREATE TABLE user_comments
   	(comment_id	int(10)	NOT NULL,
   	 user_id	int(10)	NOT	NULL,
   	 car_id		int(10)	NOT NULL,
   	 short_comments   varchar(200)	COMMENT '用户简评',
   	 comment_time       datetime NOT NULL	COMMENT '用户评论时间',
   	 rate		tinyint(1)	COMMENT '用户评分',
   	 PRIMARY KEY (comment_id),
   	 FOREIGN KEY (user_id) REFERENCES user(user_id),
   	 FOREIGN KEY (car_id) REFERENCES car(car_id));

    CREATE TABLE user_collect
  	(collect_id	int(10) NOT NULL,
  	 user_id	int(10) NOT NULL,
  	 car_id		int(10) NOT NULL,
  	 collect_time       datetime NOT NULL COMMENT '用户收藏时间',
  	 PRIMARY KEY (collect_id),
   	 FOREIGN KEY (user_id) REFERENCES user(user_id),
   	 FOREIGN KEY (car_id) REFERENCES car(car_id));

    CREATE TABLE user_view
  	(view_id	int(10) NOT NULL,
  	 user_id	int(10) NOT NULL,
  	 car_id		int(10) NOT NULL,
  	 view_time       datetime NOT NULL COMMENT '用户查看时间',
  	 PRIMARY KEY (view_id),
   	 FOREIGN KEY (user_id) REFERENCES user(user_id),
   	 FOREIGN KEY (car_id) REFERENCES car(car_id));