package com.example.springboot.Service.Impl;

import com.example.springboot.Bean.Category;
import com.example.springboot.Mapper.CategoryMapper;
import com.example.springboot.Service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
 
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Category> list() {
        List<Category> list =  categoryMapper.findAll();
        return list;
    }

    @Override
    @Cacheable(value = "category", key = "'category '+ #p0")
    public Category get(int id) {
        LOGGER.info("通过数据库获取id为: {} 的信息", id);
        return categoryMapper.get(id);
    }  
     
    @Override
    public void save(Category category) {
        categoryMapper.save(category);
    }
     
    @Override
    @CacheEvict(value = "category", key = "'category' + #id")
    public void delete(int id) {
        categoryMapper.delete(id);
    }

    @Override
    @CachePut(value = "category", condition = "#result != null ", key = "'category' + #p0.id")
    public void update(Category category) {
        categoryMapper.update(category);
    }

}