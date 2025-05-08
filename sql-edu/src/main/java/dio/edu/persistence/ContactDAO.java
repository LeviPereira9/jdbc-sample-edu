package dio.edu.persistence;

import dio.edu.persistence.entity.ContactEntity;
import dio.edu.persistence.entity.EmployeeEntity;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    public void insert(final ContactEntity entity){

        var sqlQuery = "INSERT INTO contacts (description, type, employee_id) values (?, ?, ?)";
        try(
                var connection = ConnectionUtil.getConnection();
                // Se quisermos que retorne os ID gerados, precisamos passar esse interesse na connection.
                var preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        ){

            preparedStatement.setString(1, entity.getDescription());
            preparedStatement.setString(2, entity.getType());
            preparedStatement.setLong(3, entity.getEmployee().getId());

            //De fato executa a Query
            int linhasAfetadas = preparedStatement.executeUpdate();
            // Esse mostra quantas linhas foram afetadas pela nossa insert.
            System.out.printf("Foram afetados  %d registros na base da dados \n", linhasAfetadas);


            // Pegar o valor do ultimo id gerado
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                //Seta pra gente soltar lá na frente.
                entity.setId(generatedKeys.getLong(1));
            }

        } catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    public List<ContactEntity> findByEmployeeId(final long employeeId){
        List<ContactEntity> entities = new ArrayList<>();

        String sqlQuery = "SELECT * FROM contacts WHERE employee_id = ?";

        try(
                var connection = ConnectionUtil.getConnection();
                var prepareStatement = connection.prepareStatement(sqlQuery);
                ){

            prepareStatement.setLong(1, employeeId);
            prepareStatement.execute();

            var resultSet = prepareStatement.getResultSet();
            while(resultSet.next()){
                ContactEntity entity = new ContactEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setDescription(resultSet.getString("description"));
                entity.setType(resultSet.getString("type"));
                entity.setEmployee(new EmployeeEntity());
                entity.getEmployee().setId(resultSet.getLong("employee_id"));
                entities.add(entity);
            }

        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return entities;
    }

}
