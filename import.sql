DROP DATABASE IF EXISTS usersapp;

CREATE DATABASE IF NOT EXISTS usersapp;

USE usersapp;

CREATE TABLE users(
    id VARCHAR(255) NOT NULL,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30),
    username VARCHAR(30) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    birthdate DATE NOT NULL,
    avatar VARCHAR(255),
    region_id INT NOT NULL,
    active TINYINT NOT NULL,
    google TINYINT NOT NULL,
    confirmed TINYINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,

    PRIMARY KEY(id)
);

ALTER TABLE users ADD CONSTRAINT uk_username UNIQUE(username);
ALTER TABLE users ADD CONSTRAINT uk_email UNIQUE(email);
ALTER TABLE users ADD CONSTRAINT uk_slug UNIQUE(slug);

CREATE TABLE roles(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE users_roles(
    user_id VARCHAR(255) NOT NULL,
    role_id INT NOT NULL
);

CREATE TABLE regions(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,

    PRIMARY KEY(id)
);

ALTER TABLE roles ADD CONSTRAINT uk_role_name UNIQUE(name);
ALTER TABLE users_roles ADD CONSTRAINT uk_user_role_id UNIQUE(user_id, role_id);

ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_role_id FOREIGN KEY(role_id) REFERENCES roles(id);
ALTER TABLE users_roles ADD CONSTRAINT fk_users_roles_user_id FOREIGN KEY(user_id) REFERENCES users(id);

ALTER TABLE users ADD CONSTRAINT fk_users_regions FOREIGN KEY(region_id) REFERENCES regions(id);

INSERT INTO roles (name) VALUES("ROLE_ADMIN");
INSERT INTO roles (name) VALUES("ROLE_USER");

INSERT INTO regions (name) VALUES("Norteamerica");
INSERT INTO regions (name) VALUES("Sudamerica");
INSERT INTO regions (name) VALUES("Europa");
INSERT INTO regions (name) VALUES("Asia");
INSERT INTO regions (name) VALUES("Oceania");
INSERT INTO regions (name) VALUES("Africa");

INSERT INTO users (id, firstname, lastname, username, slug, email, password, phone, birthdate, region_id, active, google, confirmed, created_at, updated_at)
VALUES('550e8400-e29b-41d4-a716-446655440000', 'Admin', 'Admin', 'admin', 'admin-1702672884056', 'admin@admin.com', '$2a$12$BX.mva.bha8VEZxOgX06TOmnqj230VsYw60jR125z4djBK8sdBjQ2', '00000', NOW(), 1, 1, 0, 1, NOW(), NOW());

INSERT INTO users_roles VALUES('550e8400-e29b-41d4-a716-446655440000', 1);
INSERT INTO users_roles VALUES('550e8400-e29b-41d4-a716-446655440000', 2);