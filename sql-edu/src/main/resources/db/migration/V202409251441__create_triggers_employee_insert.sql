DELIMITER $$
CREATE TRIGGER tgr_employee_audit_insert
    AFTER INSERT ON employees
    FOR EACH ROW
BEGIN
    INSERT INTO employees_audit (
        employee_id,
        name,
        salary,
        birthday,
        operation
    )
    VALUES (
               NEW.id,
               NEW.name,
               NEW.salary,
               NEW.birthday,
               'I'
           );
    END$$
    DELIMITER ;
