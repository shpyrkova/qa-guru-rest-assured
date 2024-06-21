package models.lombok;

import lombok.Data;

@Data
public class UsersListResponseBodyModel {
    Integer page, per_page, total, total_pages;
    Object data, support;
}
