drop table if exists sys_sentinel_rule;
create table sys_sentinel_rule(
id bigint primary key, 
rule_key varchar(255) not null,
rule_type int not null,
rule text,
env varchar(45) not null,
create_time datetime,
update_time datetime
);