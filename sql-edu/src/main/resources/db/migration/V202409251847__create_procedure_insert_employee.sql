DELIMITER $$
    CREATE PROCEDURE prc_insert_employee(
        OUT p_id BIGINT,
        IN p_name VARCHAR(150) ,
        IN p_salary decimal(10,2),
        IN p_birthday timestamp
    )
        BEGIN
            INSERT INTO employees (name, salary, birthday)
            values
                (p_name,
                 p_salary,
                 p_birthday);
            SET p_id = LAST_INSERT_ID();

END$$
DELIMITER ;