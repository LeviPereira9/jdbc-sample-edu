package dio.edu.persistence;

import com.mysql.cj.jdbc.StatementImpl;
import dio.edu.persistence.entity.ContactEntity;
import dio.edu.persistence.entity.EmployeeEntity;
import dio.edu.persistence.entity.ModuleEntity;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;

public class EmployeeDAO {
    private final ContactDAO contactDAO = new ContactDAO();
    private final AcessDAO acessDAO = new AcessDAO();

    public void insert(final EmployeeEntity entity){


        var sqlQuery = "INSERT INTO employees (name, salary, birthday) values (?, ?, ?)";
        try(
                var connection = ConnectionUtil.getConnection();
                //var statement = connection.createStatement();
                // Se quisermos que retorne os ID gerados, precisamos passar esse interesse na connection.
                var preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
                ){

                    preparedStatement.setString(1, entity.getName());
                    preparedStatement.setBigDecimal(2, entity.getSalary());
                    preparedStatement.setObject(3, formartOffsetDateTime(entity.getBirthday()));

                    int linhasAfetadas = preparedStatement.executeUpdate();


                // seta o valor da entity para o ultimo id inserido na nossa DB, pois a gente não cria esse ID,
                // é o DB mesmo. // Antigo uso aqui, esse tem mt sql injection.
                if(preparedStatement instanceof StatementImpl impl){
                    entity.setId(impl.getLastInsertID());

                    //De fato executa a Query

                    entity.getModules()
                            .stream()
                            .map(ModuleEntity::getId)
                            .forEach(
                                    (m) -> acessDAO.insert(entity.getId(),m)
                            );
                    // Esse mostra quantas linhas foram afetadas pela nossa insert.
                    System.out.printf("Foram afetados  %d registros na base da dados \n", linhasAfetadas);
                }

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

    public void insertWithProcedure(final EmployeeEntity entity){

        var sqlQuery = "INSERT INTO employees (name, salary, birthday) values (?, ?, ?)";
        try(
                var connection = ConnectionUtil.getConnection();
                var preparedStatement = connection.prepareCall("CALL prc_insert_employee(?, ?, ?, ?);");
        ){

            preparedStatement.registerOutParameter(1, Types.BIGINT);
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setBigDecimal(3, entity.getSalary());
            preparedStatement.setObject(4, formartOffsetDateTime(entity.getBirthday()));
            preparedStatement.execute();

            entity.setId(preparedStatement.getLong(1));

        } catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    public void insertBatch(final List<EmployeeEntity> entities){

        var sqlQuery = "INSERT INTO employees (name, salary, birthday) values (?, ?, ?)";
        //Testa conexão
        try(var connection = ConnectionUtil.getConnection();){
            //Texta a query.
            try(var preparedStatement = connection.prepareCall(sqlQuery);){
                connection.setAutoCommit(false);
                for(var entity : entities){
                    preparedStatement.setString(1, entity.getName());
                    preparedStatement.setBigDecimal(2, entity.getSalary());
                    preparedStatement.setObject(3, formartOffsetDateTime(entity.getBirthday()));
                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
                connection.commit();

            }catch(SQLException ex){
                //Se der algo errado na query, rollback.
                connection.rollback();
                ex.printStackTrace();
            }

        } catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    public void update(final EmployeeEntity entity){
        //Query da inserção de dados na table em questão ae.
        //Para previnir o SQL injection, é melhor deixar aqui fora a query para a gente colocar o rpeparedStatement.
        // Melhor uso de tipos: O PreparedStatement permite passar parâmetros diretamente, sem necessidade de formatação manual, como datas.
        String sqlQuery = "UPDATE employees SET name = ?, salary = ?, birthday = ? WHERE id = ?";

        try(
                var connection = ConnectionUtil.getConnection();
                var statement = connection.createStatement();
                var preparedStatement = connection.prepareStatement(sqlQuery)
        ){



            //PreparedStatement previne a injeção de SQL.
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setBigDecimal(2, entity.getSalary());
            preparedStatement.setObject(3, formartOffsetDateTime(entity.getBirthday()));
            preparedStatement.setLong(4, entity.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.printf("Foram afetadas %d registros na base da dados \n", rowsUpdated);



        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void delete(final long id){
        var sqlQuery = "DELETE FROM employees WHERE id = ?";

        try(
                var connection = ConnectionUtil.getConnection();
                var preparedStatement = connection.prepareStatement(sqlQuery);
                ){

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeeEntity> findAll(){
        List<EmployeeEntity> entities = new ArrayList<>();

        try(
                var connection = ConnectionUtil.getConnection();
                var statement = connection.createStatement();
        ){


            statement.executeQuery("SELECT * FROM employees");
            var resultSet = statement.getResultSet();
            while(resultSet.next()){
                var entity = new EmployeeEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setSalary(resultSet.getBigDecimal("salary"));

                var birthdayInstant = resultSet.getTimestamp("birthday").toInstant();
                entity.setBirthday(OffsetDateTime.ofInstant(birthdayInstant, UTC));

                entity.setContacts(contactDAO.findByEmployeeId(entity.getId()));

                entities.add(entity);
            }



        } catch(SQLException ex){
            ex.printStackTrace();
        }

        return entities;
    };

    public EmployeeEntity findById(final long id){
        var entity = new EmployeeEntity();
        //Query
        var sqlQuery = "SELECT * FROM employees INNER JOIN contacts ON contacts.employee_id = employees.id WHERE employees.id = ?";
        try(
                var connection = ConnectionUtil.getConnection();
                //var statement = connection.createStatement();
                var preparedStatement = connection.prepareStatement(sqlQuery);
        ){
            //Passa variavel
            preparedStatement.setLong(1, id);
            //Executa Query
            preparedStatement.executeQuery();

            //Pega o retorno.
            var resultSet = preparedStatement.getResultSet();

            //Se existir faz a festa.
            if(resultSet.next()){
                //Result só tem tipos, vc diferencia se tiver repetido pela coluna em que retornou a gente, isso é refletido no bd.
                entity.setId(resultSet.getLong("employees.id"));
                entity.setName(resultSet.getString("employees.name"));
                entity.setSalary(resultSet.getBigDecimal("employees.salary"));

                var birthdayInstant = resultSet.getTimestamp("birthday").toInstant();
                entity.setBirthday(OffsetDateTime.ofInstant(birthdayInstant, UTC));

                entity.setContacts(new ArrayList<>());
                do {
                    var contact = new ContactEntity();
                    contact.setId(resultSet.getLong("contacts.id"));
                    contact.setDescription(resultSet.getString("contacts.description"));
                    contact.setType(resultSet.getString("contacts.type"));
                    entity.getContacts().add(contact);
                } while(resultSet.next());


            }


        } catch(SQLException ex){
            ex.printStackTrace();
        }

        return entity;
    }

    public String formartOffsetDateTime(final OffsetDateTime dateTime){
        //Aqui a gente ta convertendo a data pra UTC e depois retornando ela com o nosso format.
        var utcDatatime = dateTime.withOffsetSameInstant(UTC);
        return utcDatatime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
