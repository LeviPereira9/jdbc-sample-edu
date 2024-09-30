package dio.edu.persistence.entity;

import lombok.Data;

import java.util.List;

@Data
public class ModulesEntity {

    private long id;

    private String name;

    private List<EmployeeEntyty> employees;

}
