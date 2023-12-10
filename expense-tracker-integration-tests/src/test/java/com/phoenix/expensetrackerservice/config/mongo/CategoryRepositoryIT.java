package com.phoenix.expensetrackerservice.config.mongo;

import com.phoenix.expensetrackerservice.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepositoryIT extends MongoRepository<Category, String> {
}
