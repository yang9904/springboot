package com.example.springboot.Controller;

import com.example.springboot.common.WebLog;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springboot.Bean.Category;
import com.example.springboot.Service.CategoryService;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/listCategory")
    @WebLog
    public String listCategory(Model m, @RequestParam(value = "start", defaultValue = "1") int start,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        PageHelper.startPage(start, size);
        List<Category> cs =categoryService.list();
        PageInfo<Category> page = new PageInfo<>(cs);
        m.addAttribute("page", page);
        return "listCategory";
    }

    @RequestMapping("/addCategory")
    public String addCategory(Category c) throws Exception {
        categoryService.save(c);
        return "redirect:listCategory";
    }

    @RequestMapping("/deleteCategory")
    public String deleteCategory(Category c) throws Exception {
        categoryService.delete(c.getId());
        return "redirect:listCategory";
    }

    @RequestMapping("/updateCategory")
    public String updateCategory(Category c) throws Exception {
        categoryService.update(c);
        return "redirect:listCategory";
    }

    @RequestMapping("/editCategory")
    public String editCategory(int id,Model m) throws Exception {
        Category c= categoryService.get(id);
        m.addAttribute("c", c);
        return "editCategory";
    }

    @RequestMapping("/getCategory")
    @WebLog
    public String getCategory(int id) throws Exception {
        Category category = categoryService.get(id);
        return "listCategory";
    }

    @RequestMapping("/loadToES")
    public String loadToES() {
        categoryService.contextLoads();
        return "listCategory";
    }

}