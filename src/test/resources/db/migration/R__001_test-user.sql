INSERT INTO users (id, email, password)
VALUES (nextval('hibernate_sequence'), 'test@banking', '$2y$12$Ii4NAbSEBoD7HO6vTYL.EOnGt26u.WY4Umkj9ZX716Ex4wV34A10K');


INSERT INTO accounts (id, balance, user_id)
VALUES (nextval('hibernate_sequence'), 0, (select max(id) from users));
