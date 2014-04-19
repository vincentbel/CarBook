INSERT INTO brand(name) VALUES('BMW');
INSERT INTO brand_series(name,brand_id) VALUES ('7series', 1);
INSERT INTO car_grade(grade) VALUES ('grand');
INSERT INTO produce_company(name, set_up_time, nationality) VALUES ('BMW', '1916-03-07', 'Germany');
INSERT INTO car(car_grade_id, price_highest, price_lowest, brand_id, series_id, model_number, view_times, time_to_market, company_id) VALUES (1, 1440000, 1170000, 1, 1, '2013 740Li grand', 1000, '2013-10-01', 1);


INSERT INTO car_body_structure(car_id, door_number, seat_number) VALUES (1, 4, 4);
