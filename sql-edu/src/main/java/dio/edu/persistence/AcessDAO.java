package dio.edu.persistence;

import java.sql.SQLException;

public class AcessDAO {

    public void insert(final long employee_id, final long module_id){


        var sqlQuery = "INSERT INTO accesses (employee_id, module_id) values (?, ?)";
        try(
                var connection = ConnectionUtil.getConnection();
                var preparedStatement = connection.prepareStatement(sqlQuery);
        ){

            preparedStatement.setLong(1, employee_id);
            preparedStatement.setLong(2, module_id);

            preparedStatement.executeUpdate();


        } catch(SQLException ex){
            ex.printStackTrace();
        }

    }
}
