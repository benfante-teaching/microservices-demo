INSERT INTO person (id, first_name, last_name, age) values (10000, 'Mario', 'Rossi', 40) ON CONFLICT DO NOTHING;
INSERT INTO person (id, first_name, last_name, age) values (10001, 'Giovanna', 'Bianchi', 23) ON CONFLICT DO NOTHING;
INSERT INTO person (id, first_name, last_name, age) values (10002, 'Carlo', 'Neri', 32) ON CONFLICT DO NOTHING;
