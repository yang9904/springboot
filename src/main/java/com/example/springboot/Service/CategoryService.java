package com.example.springboot.Service;

import com.example.springboot.Bean.Category;

import java.util.List;


public interface CategoryService {

    List<Category> list();
     
    void save(Category category);
     
    void delete(int id);

    void update(Category category);
     
    Category get(int id);
}