INSERT INTO users (id, email, password)
VALUES (nextval('hibernate_sequence'), 'test@banking', 'password');


INSERT INTO accounts (id, balance, user_id)
VALUES (nextval('hibernate_sequence'), 0, (select max(id) from users));
