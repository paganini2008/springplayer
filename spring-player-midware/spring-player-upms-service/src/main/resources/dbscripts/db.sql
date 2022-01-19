drop table if exists sys_enterprise;
CREATE TABLE sys_enterprise(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	country VARCHAR(45) NOT NULL,
	address VARCHAR(255),
	postcode VARCHAR(45),
	contact VARCHAR(45),
	phone VARCHAR(255),
	email VARCHAR(45),
	create_time DATETIME,
	update_time DATETIME
);

drop table if exists sys_emp;
CREATE TABLE sys_emp(
	id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	password VARCHAR(4000) NOT NULL,
	position VARCHAR(45),
	phone VARCHAR(45),
	email VARCHAR(45),
	enterprise_id INT NOT NULL,
	dept_id INT NOT NULL,
	enabled INT NOT NULL,
	create_time DATETIME,
	update_time DATETIME
);

drop table if exists sys_dept;
CREATE TABLE sys_dept(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(45) NOT NULL,
	pid INT NOT NULL,
	level INT NOT NULL,
	leader INT,
	enterprise_id INT NOT NULL,
	create_time DATETIME,
	update_time DATETIME
);

drop table if exists sys_role;
CREATE TABLE sys_role(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(45) NOT NULL,
	description VARCHAR(255),
	enterprise_id INT NOT NULL,
	code varchar(45) not null,
	create_time DATETIME,
	update_time DATETIME
);

drop table if exists sys_perm;
CREATE TABLE sys_perm(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(45) NOT NULL,
	path VARCHAR(255),
	enterprise_id INT NOT NULL,
	code varchar(45) not null,
	create_time DATETIME,
	update_time DATETIME
);

drop table if exists sys_role_emp;
CREATE TABLE sys_role_emp(
	id INT AUTO_INCREMENT PRIMARY KEY,
	role_id INT NOT NULL,
	emp_id INT NOT NULL
);

drop table if exists sys_role_perm;
CREATE TABLE sys_role_perm(
	id INT AUTO_INCREMENT PRIMARY KEY,
	role_id INT NOT NULL,
	perm_id INT NOT NULL,
	emp_id INT NOT NULL
);

DROP TABLE IF EXISTS sys_oauth_client_details;

CREATE TABLE sys_oauth_client_details (
  client_id varchar(45) PRIMARY KEY,
  resource_ids varchar(255) DEFAULT NULL,
  client_secret varchar(255) DEFAULT NULL,
  scope varchar(255) DEFAULT NULL,
  authorized_grant_types varchar(255) DEFAULT NULL,
  web_server_redirect_uri varchar(255) DEFAULT NULL,
  authorities varchar(255) DEFAULT NULL,
  access_token_validity int DEFAULT NULL,
  refresh_token_validity int DEFAULT NULL,
  additional_information varchar(4000) DEFAULT NULL,
  autoapprove varchar(255) DEFAULT NULL,
  tenant_id int DEFAULT NULL
);

insert into sys_oauth_client_details(client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,`additional_information`,autoapprove,tenant_id) values ('test',NULL,'123456','read,write,trust','authorization_code,password,refresh_token,implicit,client_credentials',NULL,NULL,86400,2592000,'{\"country\":\"CN\",\"country_code\":\"086\"}','true',1);


