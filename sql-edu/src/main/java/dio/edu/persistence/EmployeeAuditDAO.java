package dio.edu.persistence;

import dio.edu.persistence.entity.EmployeeAuditEntity;
import dio.edu.persistence.entity.OperationEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.isNull;

public class EmployeeAuditDAO {

    public List<EmployeeAuditEntity> findAll(){
        List<EmployeeAuditEntity> entitiesAudit = new ArrayList<>();

        try(
                var connection = ConnectionUtil.getConnection();
                var statement = connection.createStatement();
        ){


            statement.executeQuery("SELECT * FROM view_employees_audit");
            var resultSet = statement.getResultSet();

            while(resultSet.next()){



                var auditEntity = new EmployeeAuditEntity(
                        resultSet.getLong("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("old_name"),
                        resultSet.getBigDecimal("salary"),
                        resultSet.getBigDecimal("old_salary"),
                        getDateTimeOrNull(resultSet, "birthday"),
                        getDateTimeOrNull(resultSet, "old_birthday"),
                        OperationEnum.getByDbOperation(resultSet.getString("operation"))

                );


                entitiesAudit.add(auditEntity);
            }



        } catch(SQLException ex){
            ex.printStackTrace();
        }

        return entitiesAudit;
    };

    public OffsetDateTime getDateTimeOrNull(final ResultSet resultSet, final String field) throws SQLException {
        //Jeitinho pra lidar com o NullPointer da date lá, no padrão n tem como, mas no old precisa ne.
        return isNull(resultSet.getTimestamp(field)) ?
                null :
                OffsetDateTime.ofInstant(
                        resultSet.getTimestamp(field).toInstant(), UTC)
                ;
    }

}
