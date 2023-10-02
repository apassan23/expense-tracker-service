package com.phoenix.expensetrackerservice.repository;

import com.phoenix.expensetrackerservice.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByTitle(String title);
}
