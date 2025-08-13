INSERT INTO users (username, password, created_at)
VALUES ('admin', '$2a$12$ahxmaBp6aWd33GsCM17zQeN/R5l.IQc6WzobNfwVju1G2tqzyQSDq', NOW());

INSERT INTO user_roles (user_id, role)
VALUES (1, 'ROLE_ADMIN')