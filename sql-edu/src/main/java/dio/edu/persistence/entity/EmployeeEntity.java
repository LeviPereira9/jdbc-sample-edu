package dio.edu.persistence.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class EmployeeEntity {
    private long id;

    private String name;

    private BigDecimal salary;

    private OffsetDateTime birthday;

    private List<ContactEntity> contacts;

    private List<ModuleEntity> modules;


}
