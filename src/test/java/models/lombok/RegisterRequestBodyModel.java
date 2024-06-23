package models.lombok;

import lombok.Data;

@Data
public class RegisterRequestBodyModel {
    private String email, password;
}
