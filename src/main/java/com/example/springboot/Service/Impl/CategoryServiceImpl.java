package com.example.springboot.Service.Impl;

import com.example.springboot.Bean.Category;
import com.example.springboot.Mapper.CategoryMapper;
import com.example.springboot.Service.CategoryService;
import com.example.springboot.utils.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="category")
public class CategoryServiceImpl implements CategoryService {
 
    @Autowired
    CategoryMapper categoryMapper;
     
    @Override
    @Cacheable(key="'category '+#p0 + '-' + #p1 ")
    public List<Category> list(int start, int size) {
        List<Category> list = categoryMapper.findAll();
        return list;
    }
 
    @Override
    @Cacheable(key="'category '+ #p0")
    public Category get(int id) {
        return categoryMapper.get(id);
    }  
     
    @Override
    @CacheEvict(allEntries=true)
    public void save(Category category) {
        // TODO Auto-generated method stub
        categoryMapper.save(category);
    }
     
    @Override
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        // TODO Auto-generated method stub
        categoryMapper.delete(id);
    }

    @Override
    @CacheEvict(allEntries=true)
    public void update(Category category) {
        categoryMapper.update(category);
    }

}