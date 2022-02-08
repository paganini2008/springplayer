DROP TABLE IF EXISTS sys_sentinel_rule;
CREATE TABLE sys_sentinel_rule(
id BIGINT PRIMARY KEY, 
rule_key VARCHAR(255) NOT NULL,
rule_type INT NOT NULL,
rule TEXT,
env VARCHAR(45) NOT NULL,
create_time DATETIME,
update_time DATETIME
);