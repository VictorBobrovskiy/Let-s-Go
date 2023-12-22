package com.digsol.main.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Collection<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        Collection<CategoryDto> categories = categoryService.getCategories(from, size);

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long catId) {

        CategoryDto category = CategoryMapper.toCategoryDto(categoryService.getCategory(catId));

        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}