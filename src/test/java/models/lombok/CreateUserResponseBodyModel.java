package models.lombok;

import lombok.Data;

@Data
public class CreateUserResponseBodyModel {
    String name, job, id, createdAt;
}
