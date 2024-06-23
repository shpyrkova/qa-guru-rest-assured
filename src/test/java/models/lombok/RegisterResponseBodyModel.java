package models.lombok;

import lombok.Data;

@Data
public class RegisterResponseBodyModel {
    private String token, error;
    private Integer id;
}
