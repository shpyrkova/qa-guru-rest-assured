package models.lombok;

import lombok.Data;

@Data
public class CreateUserResponseBodyModel {
    private String name, job, id, createdAt;
}
