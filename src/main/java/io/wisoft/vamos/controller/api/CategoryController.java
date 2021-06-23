package io.wisoft.vamos.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.category.CategoryName;
import io.wisoft.vamos.repository.CategoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public ApiResult<List<CategoryResponse>> allCategories() {

        return ApiResult.succeed(categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList()));
    }

    @Getter
    @Setter
    protected static class CategoryResponse {

        private Long id;

        @JsonProperty("category_name")
        private String[] names = new String[2];

        CategoryResponse(Category category) {
            this.id = category.getId();
            CategoryName name = category.getName();
            String kr = name.getKr();
            String en = name.getEn();
            names[0] = kr;
            names[1] = en;
        }
    }
}
