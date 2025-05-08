CREATE TABLE accesses(
    employee_id BIGINT not null,
    module_id BIGINT not null,
    PRIMARY KEY(employee_id, module_id),
    CONSTRAINT fk_accesses_employees FOREIGN KEY (employee_id) REFERENCES employees(id),
    CONSTRAINT fk_accesses_modules FOREIGN KEY (module_id) REFERENCES modules(id)

)engine=InnoDB default charset=utf8;