CREATE TABLE employees_audit(
    id BIGINT not null auto_increment,
    name VARCHAR(150) not null,
    old_name VARCHAR(150) not null,
    salary decimal(10,2) not null,
    old_salary decimal(10,2) not null,
    birthday timestamp not null,
    old_birthday timestamp not null,
    operation CHAR(1),
    created_at timestamp default CURRENT_TIMESTAMP,
PRIMARY KEY(id)
)engine=InnoDB default charset=utf8;