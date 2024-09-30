package dio.edu.persistence;

import lombok.Data;

@Data
public class ContactEntity {
    private long id;
    private String description;
    private String type;
}
