drop table if exists sys_route;
create table sys_route(
id bigint primary key, 
service_id varchar(255) not null, 
group_name varchar(45),
env varchar(45) not null,
rule text not null,
create_time datetime,
update_time datetime
);