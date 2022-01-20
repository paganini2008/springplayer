drop table if exists sys_i18n;
CREATE TABLE sys_i18n(
	id INT AUTO_INCREMENT PRIMARY KEY,
	project VARCHAR(45) NOT NULL,
	lang VARCHAR(45) NOT NULL,
	code VARCHAR(45) NOT NULL,
	text VARCHAR(255),
	create_time DATETIME,
	update_time DATETIME
);

