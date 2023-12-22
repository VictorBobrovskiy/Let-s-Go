package com.digsol.main.category;

import com.digsol.main.error.AccessDeniedException;
import com.digsol.main.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public CategoryDto addNewCategory(CategoryDto categoryDto) {

        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));

        log.debug("New category added with id {}", category.getId());

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {

        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new AccessDeniedException("You cannot delete category with events");
        }
        categoryRepository.deleteById(catId);

        log.debug("Category with id {} deleted", catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + catId + " not found"));

        category.setName(categoryDto.getName());

        Category updatedCategory = categoryRepository.save(category);

        log.debug("Category with id {} updated", catId);

        return CategoryMapper.toCategoryDto(updatedCategory);
    }

}