INSERT INTO brand(name) VALUES('BMW');
INSERT INTO brand_series(name,brand_id) VALUES ('7series', 1);
INSERT INTO car_grade(grade) VALUES ('grand');
INSERT INTO produce_company(name, set_up_time, nationality) VALUES ('BMW', '1916-03-07', 'Germany');
INSERT INTO car(car_grade_id, price_highest, price_lowest, brand_id, series_id, model_number, view_times, time_to_market, company_id) VALUES (1, 1440000, 1170000, 1, 1, '2013 740Li grand', 1000, '2013-10-01', 1);


INSERT INTO car_body_structure(car_id, door_number, seat_number) VALUES (1, 4, 4);

INSERT INTO sale_company(name, telephone, address) VALUES ('北京星得宝','400-868-9298','北京市朝阳区高碑店乡白家楼8号');
INSERT INTO sale_company(name, telephone, address) VALUES ('北京运通星宝宝马','400-878-9298','北京市大兴区西红门中鼎路');
INSERT INTO sale_company(name, telephone, address) VALUES ('北京京宝行','400-868-5698','北京市海淀区闵庄路19号');


INSERT INTO sale(car_id, sale_company_id, sale_date) VALUES (1, 1, '2013-12-31');
INSERT INTO sale(car_id, sale_company_id, sale_date) VALUES (1, 2, '2013-12-31');
INSERT INTO sale(car_id, sale_company_id, sale_date) VALUES (1, 3, '2013-12-31');

INSERT INTO car_engine VALUES (1, 'N55B30A', 2979, '涡轮增压', 6, 'L', 4, NULL, 320, 235, 5800, '汽油', '97号', '欧IV');
INSERT INTO car_body_structure VALUES (1, 5223, 1902, 1498, NULL, 3210, NULL, 4, 5, 80, NULL);
INSERT INTO car_multimedia_configuration VALUES (1, 1, 1, 1, 1, 2, 3, 1, 1, 1, 8);
INSERT INTO car_tech_configuration VALUES (1, 0, 1, 1);