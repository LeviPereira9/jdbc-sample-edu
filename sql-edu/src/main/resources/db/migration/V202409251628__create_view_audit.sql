CREATE VIEW view_employees_audit AS
    SELECT
        employee_id,
        name,
        old_name,
        salary,
        old_salary,
        birthday,
        old_birthday,
        operation
        FROM employees_audit
    ORDER BY created_at;