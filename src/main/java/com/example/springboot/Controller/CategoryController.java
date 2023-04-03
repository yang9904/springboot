package com.example.springboot.Controller;

import java.util.List;

import com.example.springboot.Bean.Category;
import com.example.springboot.Mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {
    @Autowired
    CategoryMapper categoryMapper;
     
    @RequestMapping("/listCategory")
    public String listCategory(Model m, @RequestParam(value = "start", defaultValue = "0") int start,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        PageHelper.startPage(start,size);
        List<Category> cs = categoryMapper.findAll();
        m.addAttribute("cs", cs);
        PageInfo<Category> page = new PageInfo<>(cs);
        m.addAttribute("page", page);
        return "listCategory";
    }
     
}