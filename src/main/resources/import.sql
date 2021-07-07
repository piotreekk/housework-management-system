INSERT INTO role(id, uuid, name, description) VALUES(1, '8266e1ca-dcc8-11eb-ba80-0242ac130004', 'ADMIN', 'Administor systemu');
INSERT INTO role(id, uuid, name, description) VALUES(2, '91472f60-dcc8-11eb-ba80-0242ac130004', 'USER', 'Użytkownik');

INSERT INTO user_account(id, uuid, login, password_hash, enabled) VALUES(1, '4c704f98-dcc8-11eb-ba80-0242ac130004', 'admin', 'password', true);
INSERT INTO user_role(user_account_id, role_id) VALUES(1, 1);
INSERT INTO user_role(user_account_id, role_id) VALUES(1, 2);

INSERT INTO user_account(id, uuid, login, password_hash, enabled) VALUES(2, '5ca0041c-dcc8-11eb-ba80-0242ac130004', 'user', 'password', true);
INSERT INTO user_role(user_account_id, role_id) VALUES(2, 2);

INSERT INTO house(id, uuid, name, description, address_line1, address_line2, owner_id) VALUES(1, '8a8880b6-dcc8-11eb-ba80-0242ac130004', 'Mieszkanie przy Koncertowej 12', '3 pokojowe mieszkanie przy ulicy koncertowej 12. Cena za 1 pokój: 700zł.', 'Koncertowa 12', '20-836 Lublin', 1);
INSERT INTO house(id, uuid, name, description, address_line1, address_line2, owner_id) VALUES(2, '9e8880b6-dcc8-11eb-ba80-0242ac130004', 'Dom na przedmieściach', 'Dom 10km od centrum miasta. Cena za całość: 2600zł', 'Poligonowa 123', '11-111 Lublin', 1);

INSERT INTO house_inhabitant(id, uuid, date_from, date_to, house_id, user_id) VALUES(1, '6ec5675d-dce6-11eb-ba80-0242ac130004', '2021-07-02', '2021-07-04', 1, 1);
INSERT INTO house_inhabitant(id, uuid, date_from, date_to, house_id, user_id) VALUES(2, '6ec5675e-dce6-11eb-ba80-0242ac130004', '2021-07-04', null, 1, 1);
INSERT INTO house_inhabitant(id, uuid, date_from, date_to, house_id, user_id) VALUES(3, '6ec5675f-dce6-11eb-ba80-0242ac130004', '2021-07-01', '2021-07-03', 1, 2);

INSERT INTO housework(id, uuid, name, description, scheduled_at, status, house_id) VALUES(1, '1e8880b6-dcc8-11eb-ba80-0242ac130004', 'Sprzątanie łazienki', 'Sprzątanie łazienki: mycie prysznica, lustra, podłogi', CURRENT_DATE , 'TO_DO', 1);
INSERT INTO housework(id, uuid, name, description, scheduled_at, status, house_id) VALUES(2, '2e8880b6-dcc8-11eb-ba80-0242ac130004', 'Sprzątanie korytarza i klatki schodowej', 'Odkurzanie, mycie podłogi itd.', CURRENT_DATE , 'TO_DO', 1);
INSERT INTO housework(id, uuid, name, description, scheduled_at, status, house_id) VALUES(3, '3e8880b6-dcc8-11eb-ba80-0242ac130004', 'Sprzątanie kuchni', 'Sprzątanie kuchni: mycie kuchenki, szafek, lodówki, podłogi', CURRENT_DATE + 1 , 'TO_DO', 1);
