package com.phoenix.expensetrackerservice.repository;

import com.phoenix.expensetrackerservice.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findAllByUsername(String username);

    Optional<Category> findByCategoryIdAndUsername(String categoryId, String username);

    Optional<Category> findByTitleAndUsername(String title, String username);

    boolean existsByCategoryIdAndUsername(String categoryId, String username);
}
