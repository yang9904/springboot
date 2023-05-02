package com.example.springboot.Service.Impl;

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
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

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

    @Override
    public Goods buildGoods(Category category) {
        Goods goods = new Goods();
        goods.setId(category.getId());
        goods.setName(category.getName());
        return goods;
    }

    @Override
    public void contextLoads() {
        elasticsearchRestTemplate.createIndex(Goods.class);
        elasticsearchRestTemplate.putMapping(Goods.class);
        List<Category> categoryList = categoryMapper.findAll();
        List<Goods> goodsList = categoryList.stream().map(category -> {
            try {
                return buildGoods(category);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        categoryRepository.saveAll(goodsList);
    }

}