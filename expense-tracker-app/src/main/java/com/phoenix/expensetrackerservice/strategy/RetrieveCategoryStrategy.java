package com.phoenix.expensetrackerservice.strategy;

import com.phoenix.expensetrackerservice.model.CategoryDTO;

import java.util.List;

public interface RetrieveCategoryStrategy {
    List<CategoryDTO> retrieve(CategoryDTO categoryDTO);

    RetrieveType retrieveType();
}
