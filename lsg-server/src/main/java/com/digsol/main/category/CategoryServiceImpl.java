package com.digsol.main.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Collection<CategoryDto> getCategories(Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        Collection<CategoryDto> categories = categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());

        log.debug("{} categories found", categories.size());

        return categories;
    }

    @Override
    public Category getCategory(Long catId) {

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        log.debug("Category {} found", catId);

        return category;
    }
}