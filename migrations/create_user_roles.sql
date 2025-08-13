CREATE TABLE user_roles (
	user_id BIGINT NOT NULL,
	role VARCHAR(255) NOT NULL,
	PRIMARY KEY (user_id, role),
	CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);