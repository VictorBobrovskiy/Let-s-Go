package com.digsol.main.category;

public interface AdminCategoryService {

    CategoryDto addNewCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);
}
