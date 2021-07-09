package io.wisoft.vamos.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.category.CategoryName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

    private Long id;

    @JsonProperty("category_name")
    private String[] names = new String[2];

    public CategoryResponse(Category category) {
        this.id = category.getId();
        CategoryName name = category.getName();
        String kr = name.getKr();
        String en = name.getEn();
        names[0] = kr;
        names[1] = en;
    }
}
