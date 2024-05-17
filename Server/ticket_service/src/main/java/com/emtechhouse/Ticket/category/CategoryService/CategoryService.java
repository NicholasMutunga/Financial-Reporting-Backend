package com.emtechhouse.Ticket.category.CategoryService;

import com.emtechhouse.Ticket.Utils.DataNotFoundException;
import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.category.CategoryRepository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category addCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }


    public Category updateCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            log.info("Catched Error {} " + e);
            return null;
        }
    }

    public Category findById(Long id){
        try{
            return categoryRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Data " + id +"was not found"));
        } catch (Exception e) {
            log.info("Catched Error {} "+e);
            return null;
        }
    }
}
