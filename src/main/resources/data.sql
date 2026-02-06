-- Initial Data for Conflict Tracker API

-- Countries
INSERT INTO countries (name, code) VALUES ('Ukraine', 'UKR');
INSERT INTO countries (name, code) VALUES ('Russia', 'RUS');
INSERT INTO countries (name, code) VALUES ('United States', 'USA');
INSERT INTO countries (name, code) VALUES ('Israel', 'ISR');
INSERT INTO countries (name, code) VALUES ('Palestine', 'PSE');
INSERT INTO countries (name, code) VALUES ('Syria', 'SYR');
INSERT INTO countries (name, code) VALUES ('Iran', 'IRN');
INSERT INTO countries (name, code) VALUES ('Saudi Arabia', 'SAU');
INSERT INTO countries (name, code) VALUES ('Yemen', 'YEM');
INSERT INTO countries (name, code) VALUES ('Turkey', 'TUR');
INSERT INTO countries (name, code) VALUES ('Ethiopia', 'ETH');
INSERT INTO countries (name, code) VALUES ('Eritrea', 'ERI');
INSERT INTO countries (name, code) VALUES ('Sudan', 'SDN');
INSERT INTO countries (name, code) VALUES ('Myanmar', 'MMR');
INSERT INTO countries (name, code) VALUES ('China', 'CHN');
INSERT INTO countries (name, code) VALUES ('Taiwan', 'TWN');

-- Conflicts
INSERT INTO conflicts (name, start_date, status, description) VALUES
('Russo-Ukrainian War', '2022-02-24', 'ACTIVE', 'Full-scale invasion of Ukraine by Russia. The conflict began with the annexation of Crimea in 2014 and escalated to full-scale war in 2022.');

INSERT INTO conflicts (name, start_date, status, description) VALUES
('Israeli-Palestinian Conflict', '1948-05-14', 'ACTIVE', 'Ongoing conflict between Israelis and Palestinians over territory, security, and self-determination.');

INSERT INTO conflicts (name, start_date, status, description) VALUES
('Syrian Civil War', '2011-03-15', 'ACTIVE', 'Multi-sided civil war in Syria involving the Syrian government, various rebel groups, and extremist organizations.');

INSERT INTO conflicts (name, start_date, status, description) VALUES
('Yemeni Civil War', '2014-09-16', 'ACTIVE', 'Civil war between the internationally recognized government and Houthi rebels, with Saudi-led coalition involvement.');

INSERT INTO conflicts (name, start_date, status, description) VALUES
('Tigray War', '2020-11-04', 'ENDED', 'Armed conflict in Ethiopia''s Tigray Region between the Ethiopian federal government and the Tigray People''s Liberation Front.');

INSERT INTO conflicts (name, start_date, status, description) VALUES
('Myanmar Civil War', '2021-02-01', 'ACTIVE', 'Armed conflict following the 2021 military coup, involving the military junta and various resistance groups.');

INSERT INTO conflicts (name, start_date, status, description) VALUES
('Taiwan Strait Crisis', '1954-09-03', 'FROZEN', 'Geopolitical tensions between the People''s Republic of China and Taiwan regarding sovereignty and cross-strait relations.');

INSERT INTO conflicts (name, start_date, status, description) VALUES
('Sudanese Civil War', '2023-04-15', 'ACTIVE', 'Armed conflict between the Sudanese Armed Forces and the paramilitary Rapid Support Forces.');

-- Conflict-Country relationships
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (1, 1); -- Russo-Ukrainian War - Ukraine
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (1, 2); -- Russo-Ukrainian War - Russia
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (2, 4); -- Israeli-Palestinian - Israel
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (2, 5); -- Israeli-Palestinian - Palestine
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (3, 6); -- Syrian Civil War - Syria
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (3, 7); -- Syrian Civil War - Iran
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (3, 10); -- Syrian Civil War - Turkey
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (4, 9); -- Yemeni Civil War - Yemen
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (4, 8); -- Yemeni Civil War - Saudi Arabia
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (5, 11); -- Tigray War - Ethiopia
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (5, 12); -- Tigray War - Eritrea
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (6, 14); -- Myanmar Civil War - Myanmar
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (7, 15); -- Taiwan Strait - China
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (7, 16); -- Taiwan Strait - Taiwan
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (8, 13); -- Sudanese Civil War - Sudan

-- Factions
INSERT INTO factions (name, conflict_id) VALUES ('Ukrainian Armed Forces', 1);
INSERT INTO factions (name, conflict_id) VALUES ('Russian Armed Forces', 1);
INSERT INTO factions (name, conflict_id) VALUES ('Israel Defense Forces', 2);
INSERT INTO factions (name, conflict_id) VALUES ('Hamas', 2);
INSERT INTO factions (name, conflict_id) VALUES ('Syrian Arab Army', 3);
INSERT INTO factions (name, conflict_id) VALUES ('Syrian National Army', 3);
INSERT INTO factions (name, conflict_id) VALUES ('Houthi Movement', 4);
INSERT INTO factions (name, conflict_id) VALUES ('Saudi-led Coalition', 4);
INSERT INTO factions (name, conflict_id) VALUES ('TPLF', 5);
INSERT INTO factions (name, conflict_id) VALUES ('Ethiopian National Defense Force', 5);
INSERT INTO factions (name, conflict_id) VALUES ('Tatmadaw', 6);
INSERT INTO factions (name, conflict_id) VALUES ('National Unity Government', 6);
INSERT INTO factions (name, conflict_id) VALUES ('Sudanese Armed Forces', 8);
INSERT INTO factions (name, conflict_id) VALUES ('Rapid Support Forces', 8);

-- Faction-Country (supporting countries)
INSERT INTO faction_country (faction_id, country_id) VALUES (1, 3); -- Ukrainian Forces - USA support
INSERT INTO faction_country (faction_id, country_id) VALUES (2, 2); -- Russian Forces - Russia
INSERT INTO faction_country (faction_id, country_id) VALUES (3, 4); -- IDF - Israel
INSERT INTO faction_country (faction_id, country_id) VALUES (3, 3); -- IDF - USA support
INSERT INTO faction_country (faction_id, country_id) VALUES (4, 5); -- Hamas - Palestine
INSERT INTO faction_country (faction_id, country_id) VALUES (4, 7); -- Hamas - Iran support
INSERT INTO faction_country (faction_id, country_id) VALUES (5, 6); -- Syrian Army - Syria
INSERT INTO faction_country (faction_id, country_id) VALUES (5, 7); -- Syrian Army - Iran support
INSERT INTO faction_country (faction_id, country_id) VALUES (5, 2); -- Syrian Army - Russia support
INSERT INTO faction_country (faction_id, country_id) VALUES (6, 10); -- Syrian National Army - Turkey support
INSERT INTO faction_country (faction_id, country_id) VALUES (7, 7); -- Houthis - Iran support
INSERT INTO faction_country (faction_id, country_id) VALUES (8, 8); -- Saudi Coalition - Saudi Arabia

-- Events
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2022-02-24', 'Kyiv', 'Russia launches full-scale invasion of Ukraine', 1);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2022-04-02', 'Bucha', 'Discovery of mass civilian casualties in Bucha', 1);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2022-09-30', 'Moscow', 'Russia announces annexation of four Ukrainian regions', 1);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2023-06-06', 'Nova Kakhovka', 'Kakhovka Dam destroyed, causing major flooding', 1);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2024-08-06', 'Kursk', 'Ukrainian forces launch incursion into Kursk Oblast', 1);

INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2023-10-07', 'Gaza Border', 'Hamas launches surprise attack on southern Israel', 2);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2023-10-27', 'Gaza', 'Israel launches ground operation in Gaza', 2);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2024-01-26', 'The Hague', 'ICJ orders Israel to prevent genocide in Gaza', 2);

INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2011-03-15', 'Daraa', 'Anti-government protests begin in Syria', 3);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2015-09-30', 'Latakia', 'Russia begins military intervention in Syria', 3);
INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2024-12-08', 'Damascus', 'Opposition forces capture Damascus; Assad regime falls', 3);

INSERT INTO events (event_date, location, description, conflict_id) VALUES
('2022-11-02', 'Addis Ababa', 'Cessation of hostilities agreement signed', 5);

