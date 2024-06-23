package models.lombok;

import lombok.Data;

@Data
public class CreateUserRequestBodyModel {
    private String name, job;
}
