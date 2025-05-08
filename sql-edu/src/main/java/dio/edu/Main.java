package dio.edu;

import dio.edu.persistence.*;
import dio.edu.persistence.entity.EmployeeEntity;
import dio.edu.persistence.entity.ModuleEntity;
import net.datafaker.Faker;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private final static EmployeeDAO employeeDAO = new EmployeeDAO();
    private final static EmployeeAuditDAO employeeAuditDAO = new EmployeeAuditDAO();
    private final static Faker faker = new Faker(new Locale("pt", "BR"));
    private final static ContactDAO contactDAO = new ContactDAO();
    private final static ModuleDAO moduleDAO = new ModuleDAO();

    public static void main(String[] args) {

        // Configuração do Flyway, ele que vai passar os comandos SQL para o banco de dados.
        var flyway = Flyway.configure()
                .dataSource("jdbc:mysql://localhost/jdbc_sample","root","mda@")
                .load();
        flyway.migrate();

        /*var insert = new EmployeeEntyty();
        insert.setName("Kanye West, THE GOAT!!!");
        insert.setSalary(new BigDecimal("30000000"));
        insert.setBirthday(OffsetDateTime.now().minusYears(14));
        System.out.println(insert);
        //With procedure
        employeeDAO.insertWithProcedure(insert);
        //Normal add
        //employeeDAO.insert(insert);
        System.out.println(insert);*/

        //Utilizando findAll
        //employeeDAO.findAll().forEach(System.out::println);

        //Utilizando findByid
        //System.out.println(employeeDAO.findById(1));

        //Update
        /*var update = new EmployeeEntyty();
        update.setId(10);
        update.setName("The GOAT");
        update.setSalary(new BigDecimal("15000.00"));
        update.setBirthday(OffsetDateTime.now().minusYears(15));
        employeeDAO.update(update);*/

        //Delete
        //employeeDAO.delete(2);

        //AuditDao
        //employeeAuditDAO.findAll().forEach(System.out::println);

        //Adicioanar por batch
        /*var entities = Stream.generate(()-> {
            var employee = new EmployeeEntyty();
            employee.setName(faker.name().fullName());
            employee.setSalary(new BigDecimal(faker.number().numberBetween(0,9999)));
            employee.setBirthday(OffsetDateTime.now().minusYears(faker.number().numberBetween(40,20)));

            return employee;
        })
                .limit(100)
                .toList();

        employeeDAO.insertBatch(entities);*/

       /* var userToUseInContact = new EmployeeEntyty();
        userToUseInContact.setName("The Goat");
        userToUseInContact.setSalary(new BigDecimal("300000"));
        userToUseInContact.setBirthday(OffsetDateTime.now().minusYears(18));
        employeeDAO.insert(userToUseInContact);

        //Adicionar contato
        var insertContact = new ContactEntity();
        insertContact.setDescription("TheGoat@gmail.com");
        insertContact.setType("E-mail GOAT");
        insertContact.setEmployee(userToUseInContact);
        contactDAO.insert(insertContact);

        var insertContact2 = new ContactEntity();
        insertContact2.setDescription("4002-GOAT-8922");
        insertContact2.setType("Telefone GOAT");
        insertContact2.setEmployee(userToUseInContact);
        contactDAO.insert(insertContact2);*/


        //System.out.println(employeeDAO.findById(376));

        //employeeDAO.findAll().forEach(System.out::println);

        //Criar os modulos
        //moduleDAO.insert("Financeiro");
        //moduleDAO.insert("Controle de ponto");


        /*var entities = Stream.generate(()-> {
                    var employee = new EmployeeEntity();
                    employee.setName(faker.name().fullName());
                    employee.setSalary(new BigDecimal(faker.number().numberBetween(0,9999)));
                    employee.setBirthday(OffsetDateTime.now().minusYears(faker.number().numberBetween(40,20)));
                    employee.setModules(new ArrayList<>());
                    var moduleAmount = faker.number().numberBetween(3,4);
                    for (int i = 0; i < moduleAmount; i++) {
                        var module = new ModuleEntity();
                        module.setId(i+1);
                        employee.getModules().add(module);
                    }
                    return employee;
                })
                .limit(3)
                .toList();

        entities.forEach(employeeDAO::insert);*/


        //moduleDAO.findAll().forEach(System.out::println);

         // Esse bloco faz a conexão lá com o banco de dados, mas não é aqui.
        /*try(var connection = ConnectionUtil.getConnection()){
            System.out.println("Conectou.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }*/


    }
}