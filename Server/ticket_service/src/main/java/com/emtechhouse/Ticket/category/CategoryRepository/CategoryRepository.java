package com.emtechhouse.Ticket.category.CategoryRepository;

import com.emtechhouse.Ticket.category.CategoryModel.Category;
import com.emtechhouse.Ticket.priority.PriorityModel.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    Optional<Category> findById(Long aLong);

    Optional<Category> findByCategoryCode(String CategoryCode);

    Optional<Category> findByEntityIdAndCategoryCodeAndDeletedFlag(String entityId, String categoryCode, Character flag);

    List<Category> findAllByEntityIdAndDeletedFlagOrderByIdDesc(String entityId, Character flag);

    Optional<Category>findByEntityIdAndCategoryCode(String entityId, String categoryCode);
}
