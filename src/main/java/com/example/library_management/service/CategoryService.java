package com.example.library_management.service;

import com.example.library_management.dto.CategoryRequest;
import com.example.library_management.dto.CategoryResponse;
import com.example.library_management.entity.Category;
import com.example.library_management.exception.DuplicateResourceException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }
    private CategoryResponse pack(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        return  response;
    }
     public CategoryResponse create(CategoryRequest request){
        if(categoryRepository.existsByNameIgnoreCase(request.getName()))
            throw  new DuplicateResourceException("This category already exist");
          Category category=new Category();
          category.setName(request.getName());
          category.setDescription(request.getDescription());
          Category categoryRes=categoryRepository.save(category);
          CategoryResponse categoryResponse=new CategoryResponse();
          categoryResponse.setName(categoryRes.getName());
          categoryResponse.setId(categoryRes.getId());
          categoryResponse.setDescription(categoryRes.getDescription());
          return categoryResponse;
    }
    public Page<CategoryResponse> getAll(int page, int size){
        Pageable pageable= PageRequest.of(page, size);
        Page<Category> categories=categoryRepository.findAll(pageable);
 return categories.map(this::pack);
    }
    public CategoryResponse update(Long id,CategoryRequest request){
        Category category=categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found"));

        if(request.getName()!=null)category.setName(request.getName());
        if(request.getDescription()!=null)category.setDescription(request.getDescription());
        Category categoryRes=categoryRepository.save(category);
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setName(categoryRes.getName());
        categoryResponse.setId(categoryRes.getId());
        categoryResponse.setDescription(categoryRes.getDescription());
        return categoryResponse;
    }
    public void delete(Long id){
        Category category=categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category not found"));
        if(!category.getBooks().isEmpty()) throw new IllegalArgumentException("This category can't be delete");
        categoryRepository.delete(category);
    }
}
