drop table if exists sys_route_file;
create table sys_route_file(
  id bigint primary key, 
  file_name varchar(255) not null, 
  env varchar(45) not null,
  content text not null,
  format varchar(45) not null,
  create_time datetime,
  update_time datetime
);