package com.grocerystore.service;
import com.grocerystore.entity.Category;
import com.grocerystore.exception.ResourceNotFoundException;
import com.grocerystore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAll() { return categoryRepository.findByActiveTrue(); }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    public Category create(Category category) { return categoryRepository.save(category); }

    public Category update(Long id, Category updated) {
        Category c = getById(id);
        c.setName(updated.getName()); c.setDescription(updated.getDescription());
        c.setImageUrl(updated.getImageUrl()); c.setActive(updated.isActive());
        return categoryRepository.save(c);
    }

    public void delete(Long id) {
        Category c = getById(id);
        c.setActive(false);
        categoryRepository.save(c);
    }
}
