package com.example.springboot.Service;

import com.example.springboot.Bean.Category;
import com.example.springboot.utils.Page4Navigator;
import org.springframework.data.domain.Pageable;


public interface CategoryService {
 
    public Page4Navigator<Category> list(Pageable pageable);
     
    public void save(Category category);
     
    public void delete(int id);
     
    public Category get(int id);
}