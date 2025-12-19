-- Countries
INSERT INTO country (id, name, code) VALUES (1, 'Ukraine', 'UKR');
INSERT INTO country (id, name, code) VALUES (2, 'Russia', 'RUS');
INSERT INTO country (id, name, code) VALUES (3, 'United States', 'USA');
INSERT INTO country (id, name, code) VALUES (4, 'Israel', 'ISR');
INSERT INTO country (id, name, code) VALUES (5, 'Palestine', 'PSE');

-- Conflicts
INSERT INTO conflict (id, name, start_date, status, description) VALUES (1, 'Russo-Ukrainian War', '2014-02-20', 'ACTIVE', 'Ongoing conflict between Russia and Ukraine.');
INSERT INTO conflict (id, name, start_date, status, description) VALUES (2, 'Israel-Hamas War', '2023-10-07', 'ACTIVE', 'Conflict between Israel and Hamas led Palestinian militant groups.');

-- Conflict Countries (Many-to-Many)
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (1, 1); -- Ukraine in Russo-Ukrainian War
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (1, 2); -- Russia in Russo-Ukrainian War
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (2, 4); -- Israel in Israel-Hamas War
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (2, 5); -- Palestine in Israel-Hamas War

-- Factions
INSERT INTO faction (id, name, conflict_id) VALUES (1, 'Armed Forces of Ukraine', 1);
INSERT INTO faction (id, name, conflict_id) VALUES (2, 'Russian Armed Forces', 1);
INSERT INTO faction (id, name, conflict_id) VALUES (3, 'Israel Defense Forces', 2);
INSERT INTO faction (id, name, conflict_id) VALUES (4, 'Hamas', 2);

-- Faction Supporting Countries (Many-to-Many)
INSERT INTO faction_countries (faction_id, country_id) VALUES (1, 3); -- USA supports Ukraine
INSERT INTO faction_countries (faction_id, country_id) VALUES (3, 3); -- USA supports Israel

-- Events
INSERT INTO event (id, event_date, location, description, conflict_id) VALUES (1, '2022-02-24', 'Kyiv', 'Full-scale invasion of Ukraine by Russia.', 1);
INSERT INTO event (id, event_date, location, description, conflict_id) VALUES (2, '2023-10-07', 'Southern Israel', 'Hamas-led attack on Israel.', 2);

