package com.example.springboot.Mapper;

import java.util.List;

import com.example.springboot.Bean.Category;
import org.apache.ibatis.annotations.Mapper;
 
@Mapper
public interface CategoryMapper {

    List<Category> findAll();
 
}