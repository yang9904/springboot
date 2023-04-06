package com.example.springboot.Mapper;

import java.util.List;

import com.example.springboot.Bean.Category;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface CategoryMapper {

    List<Category> findAll();

    public int save(Category category);

    public void delete(int id);

    public Category get(int id);

    public int update(Category category);

}