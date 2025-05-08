CREATE TABLE employees(
    id BIGINT not null auto_increment,
    name VARCHAR(150) not null,
    salary decimal(10,2) not null,
    birthday timestamp not null,
    PRIMARY KEY(id)

)engine=InnoDB default charset=utf8;