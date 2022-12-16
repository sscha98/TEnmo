BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);
CREATE TABLE transfer_type (
  transfer_type_id serial NOT NULL,
  transfer_type_desc varchar(10) NOT NULL,
  CONSTRAINT PK_transfer_type PRIMARY KEY (transfer_type_id)
);
-- Sequence to start user_id values at 3001 instead of 1
CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;
CREATE TABLE transfer (
  transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
  transfer_type_id int NOT NULL,
  sender_id int NOT NULL,
  receiver_id int NOT NULL,
  --sender_name varchar(32) NOT NULL,
  --receiver_name varchar(32),
  transfer_amount numeric(13, 2) NOT NULL,
  CONSTRAINT pk_transfer_transfer_id PRIMARY KEY (transfer_id),
  CONSTRAINT FK_transfer_sender_id FOREIGN KEY (sender_id) REFERENCES tenmo_user(user_id),
  CONSTRAINT FK_transfer_receiver_id FOREIGN KEY (receiver_id) REFERENCES tenmo_user(user_id),
  --CONSTRAINT FK_transfer_sender_name FOREIGN KEY (sender_name) REFERENCES tenmo_user (username),
  --CONSTRAINT FK_transfer_receiver_name FOREIGN KEY (receiver_name) REFERENCES tenmo_user (username),
  CONSTRAINT FK_transfer_transfer_type FOREIGN KEY (transfer_type_id) REFERENCES transfer_type (transfer_type_id)
);
INSERT INTO transfer_type (transfer_type_desc) VALUES ('Receive');
INSERT INTO transfer_type (transfer_type_desc) VALUES ('Send');



INSERT INTO tenmo_user (username, password_hash)
VALUES ('bob', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
       ('user', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy');

INSERT INTO transfer (transfer_id,transfer_type_id,sender_id,receiver_id,transfer_amount)
VALUES (3001,1,1001,1002,50.00);
INSERT INTO transfer (transfer_id,transfer_type_id,sender_id,receiver_id,transfer_amount)
VALUES (3002,1,1002,1001,100.00);



COMMIT;