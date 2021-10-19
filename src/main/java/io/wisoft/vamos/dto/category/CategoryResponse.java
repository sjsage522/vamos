package io.wisoft.vamos.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.category.CategoryName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("카테고리 공통 응답")
public class CategoryResponse {

    @ApiModelProperty(
            value = "카테고리 고유 id",
            name = "id",
            example = "1"
    )
    private Long id;

    @ApiModelProperty(
            value = "카테고리 명(EN, KR)",
            name = "names",
            example = "DIGITAL_DEVICE, 디지털기기"
    )
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
