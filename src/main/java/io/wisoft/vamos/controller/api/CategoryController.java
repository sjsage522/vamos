package io.wisoft.vamos.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.category.CategoryResponse;
import io.wisoft.vamos.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Api(tags = "CategoryController")
@RestController
@RequestMapping("api")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @ApiOperation(value = "카테고리 리스트 조회", notes = "모든 카테고리를 조회 합니다.")
    @GetMapping("/categories")
    public ApiResult<List<CategoryResponse>> getCategories() {

        return ApiResult.succeed(categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList()));
    }
}
