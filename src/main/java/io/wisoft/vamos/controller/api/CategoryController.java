package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.category.CategoryResponse;
import io.wisoft.vamos.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
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
}
