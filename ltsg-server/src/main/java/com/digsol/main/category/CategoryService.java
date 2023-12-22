package com.digsol.main.category;

import java.util.Collection;

public interface CategoryService {

    Collection<CategoryDto> getCategories(Integer from, Integer size);

    Category getCategory(Long catId);
}
