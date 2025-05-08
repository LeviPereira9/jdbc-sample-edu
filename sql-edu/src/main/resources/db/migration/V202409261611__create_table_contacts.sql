CREATE TABLE contacts (
    id BIGINT not null AUTO_INCREMENT,
    description VARCHAR(50) not null,
    type VARCHAR(30),
    employee_id BIGINT not null,
    CONSTRAINT fk_contact_employee FOREIGN KEY(employee_id) REFERENCES employees(id),
    PRIMARY KEY(id)
);