package com.example.springboot.repository;

import com.example.springboot.Bean.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryRepository extends ElasticsearchRepository<Category, Integer> {
}
