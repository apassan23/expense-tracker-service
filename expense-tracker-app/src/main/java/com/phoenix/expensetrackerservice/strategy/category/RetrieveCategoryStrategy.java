package com.phoenix.expensetrackerservice.strategy.category;

import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.strategy.RetrieveType;

import java.util.List;

public interface RetrieveCategoryStrategy {
    List<CategoryDTO> retrieve(CategoryDTO categoryDTO);

    RetrieveType retrieveType();
}
