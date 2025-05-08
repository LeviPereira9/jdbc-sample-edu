package dio.edu.persistence;

import dio.edu.persistence.entity.EmployeeEntity;
import dio.edu.persistence.entity.ModuleEntity;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class ModuleDAO {

    public void insert(final String name){


        var sqlQuery = "INSERT INTO modules (name) values (?)";
        try(
                var connection = ConnectionUtil.getConnection();
                var preparedStatement = connection.prepareStatement(sqlQuery);
        ){

            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();


        } catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    public List<ModuleEntity> findAll(){
        List<ModuleEntity> entities = new ArrayList<>();

        var sqlQuery = "SELECT " +
                "m.id AS module_id, " +
                "m.name AS module_name, " +
                "e.id AS employee_id, " +
                "e.name AS employee_name, " +
                "e.salary AS employee_salary, " +
                "e.birthday AS employee_birthday " +
                    "FROM modules m " +
                        "INNER JOIN accesses a " +
                            "ON m.id = a.module_id " +
                        "INNER JOIN employees e " +
                            "ON e.id = a.employee_id " +
                "ORDER BY m.id";

        try(
                var connection = ConnectionUtil.getConnection();
                var statement = connection.prepareStatement(sqlQuery);
        ){


            statement.executeQuery();
            var resultSet = statement.getResultSet();
            var hasNext = resultSet.next();
            while(hasNext){
                ModuleEntity module = new ModuleEntity();
                module.setId(resultSet.getLong("module_id"));
                module.setName(resultSet.getString("module_name"));

                module.setEmployees(new ArrayList<>());


                do{

                    var employee = new EmployeeEntity();
                    employee.setId(resultSet.getLong("employee_id"));
                    employee.setName(resultSet.getString("employee_name"));
                    employee.setSalary(resultSet.getBigDecimal("employee_salary"));

                    var birthdayInstant = resultSet.getTimestamp("employee_birthday").toInstant();
                    employee.setBirthday(OffsetDateTime.ofInstant(birthdayInstant, UTC));

                    module.getEmployees().add(employee);

                    //Vai passar os modules.
                    hasNext = resultSet.next();

                } while ((hasNext) && (module.getId() == resultSet.getLong("module_id")));

                entities.add(module);

            }



        } catch(SQLException ex){
            ex.printStackTrace();
        }

        return entities;
    };

}
