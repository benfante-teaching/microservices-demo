INSERT INTO person (id, first_name, last_name) values (10000, 'Mario', 'Rossi') ON CONFLICT DO NOTHING;
INSERT INTO person (id, first_name, last_name) values (10001, 'Giovanna', 'Bianchi') ON CONFLICT DO NOTHING;
INSERT INTO person (id, first_name, last_name) values (10002, 'Carlo', 'Neri') ON CONFLICT DO NOTHING;
