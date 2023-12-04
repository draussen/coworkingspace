-- roles
INSERT INTO role (name) VALUES ('Mitglied');
INSERT INTO role (name) VALUES ('Admin');
INSERT INTO role (name) VALUES ('Besucher');

-- application users
INSERT INTO applicationuser (email, password, name, surname, role_id) VALUES ('user1@example.com', 'password1', 'John', 'Doe', 1);
INSERT INTO applicationuser (email, password, name, surname, role_id) VALUES ('user2@example.com', 'password2', 'Jane', 'Smith', 2);

-- bookings
INSERT INTO booking (date, type, status, user_id) VALUES (CURRENT_DATE + INTERVAL '5 day', 'halber Tag', 'angenommen', 1);
INSERT INTO booking (date, type, status, user_id) VALUES (CURRENT_DATE + INTERVAL '2 day', 'ganzer Tag', 'offen', 1);
INSERT INTO booking (date, type, status, user_id) VALUES (CURRENT_DATE + INTERVAL '10 day', 'ganzer Tag', 'abgelehnt', 2);
