package com.example.springboot.Controller;

import java.util.List;

import com.example.springboot.Bean.Category;
import com.example.springboot.Mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
  
@Controller
public class CategoryController {
    @Autowired
    CategoryMapper categoryMapper;
     
    @RequestMapping("/listCategory")
    public String listCategory(Model m) {
        List<Category> cs = categoryMapper.findAll();
         
        m.addAttribute("cs", cs);
         
        return "listCategory";
    }
     
}