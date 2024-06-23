package models.lombok;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsersListResponseBodyModel {
    private Integer page, total;

    @JsonProperty("per_page")
    private Integer perPage;

    @JsonProperty("total_pages")
    private Integer totalPages;

    private Object data, support;
}
