package com.phoenix.expensetrackerservice.service.category;

import com.phoenix.expensetrackerservice.model.CategoryDTO;

public interface CategoryRequestValidationService {
    void validateForCreate(CategoryDTO categoryDTO);

    void validateForChange(CategoryDTO categoryDTO);

    void validateForRetrieve(CategoryDTO categoryDTO);

    void validateForDelete(CategoryDTO categoryDTO);
}
