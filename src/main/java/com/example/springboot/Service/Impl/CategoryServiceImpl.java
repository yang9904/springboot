package com.example.springboot.Service.Impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.example.springboot.Bean.Category;
import com.example.springboot.Bean.Goods;
import com.example.springboot.Mapper.CategoryMapper;
import com.example.springboot.Service.CategoryService;
import com.example.springboot.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private ElasticsearchClient client;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public void contextAll() throws IOException {
        Goods goods = new Goods();
        goods.setId("1212");
        goods.setName("搜索引擎测试1");
        IndexRequest<Object> indexRequest = new IndexRequest.Builder<>()
                .index("products")
                .id(goods.getId())
                .document(goods)
                .build();
        client.index(indexRequest);
        Goods goods1 = new Goods();
        goods1.setId("1213");
        goods1.setName("搜索引擎测试2");
        client.index(builder -> builder
                .index("products")
                .id(goods1.getId())
                .document(goods1)
        );
    }

}