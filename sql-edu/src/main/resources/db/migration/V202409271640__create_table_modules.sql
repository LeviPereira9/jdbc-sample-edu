CREATE TABLE modules(
    id BIGINT not null auto_increment,
    name VARCHAR(150) not null,
    PRIMARY KEY(id)
)engine=InnoDB default charset=utf8;