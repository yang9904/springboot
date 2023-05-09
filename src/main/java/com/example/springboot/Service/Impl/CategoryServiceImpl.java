package com.example.springboot.Service.Impl;

import com.example.springboot.Bean.Category;
import com.example.springboot.Bean.Goods;
import com.example.springboot.Mapper.CategoryMapper;
import com.example.springboot.Service.CategoryService;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

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

    public Goods buildGoods(Category category) {
        Goods goods = new Goods();
        goods.setName(category.getName());
        return goods;
    }

    @Override
    public void save(Goods goods) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(goods.getId())
                .withObject(goods)
                .build();
    }

    @Override
    public void contextAll() {

    }

    @Override
    public Goods query(String id) {
        return new Goods();
    }

}