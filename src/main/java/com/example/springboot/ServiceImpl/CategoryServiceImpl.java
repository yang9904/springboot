package com.example.springboot.ServiceImpl;

import com.example.springboot.Bean.Category;
import com.example.springboot.Mapper.CategoryMapper;
import com.example.springboot.Service.CategoryService;
import com.example.springboot.utils.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
 
@Service
@CacheConfig(cacheNames="category")
public class CategoryServiceImpl implements CategoryService {
 
    @Autowired
    CategoryMapper categoryMapper;
     
    @Override
    @Cacheable(key="'category '+#p0.offset + '-' + #p0.pageSize ")
    public Page4Navigator<Category> list(Pageable pageable) {
        Page<Category> pageFromJPA=  categoryMapper.findAll(pageable);
        Page4Navigator<Category> page = new Page4Navigator<>(pageFromJPA,5);
        return page;
    }
 
    @Override
    @Cacheable(key="'category '+ #p0")
    public Category get(int id) {
        Category c =categoryMapper.get(id);
        return c;
    }  
     
    @Override
    @CacheEvict(allEntries=true)
//  @CachePut(key="'category '+ #p0")
    public void save(Category category) {
        // TODO Auto-generated method stub
        categoryMapper.save(category);
    }
     
    @Override
    @CacheEvict(allEntries=true)
//  @CacheEvict(key="'category '+ #p0")
    public void delete(int id) {
        // TODO Auto-generated method stub
        categoryMapper.delete(id);
    }
 
}