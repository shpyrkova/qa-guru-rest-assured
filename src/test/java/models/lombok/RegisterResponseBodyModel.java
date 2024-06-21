package models.lombok;

import lombok.Data;

@Data
public class RegisterResponseBodyModel {
    String token, error;
    Integer id;
}
