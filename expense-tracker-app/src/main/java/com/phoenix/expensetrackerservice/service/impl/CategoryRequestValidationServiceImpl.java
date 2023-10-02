package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryRequestValidationService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryRequestValidationServiceImpl implements CategoryRequestValidationService {
    @Override
    public void validateForCreate(CategoryDTO categoryDTO) {
        if(Objects.nonNull(categoryDTO) && Objects.nonNull(categoryDTO.getTitle())) {
            return;
        }
        throw new ExpenseTrackerBadRequestException(ExpenseError.BAD_REQUEST.getDescription(), ExpenseError.BAD_REQUEST);
    }

    @Override
    public void validateForChange(CategoryDTO categoryDTO) {
        if(Objects.nonNull(categoryDTO) &&
                Objects.nonNull(categoryDTO.getCategoryId()) &&
                Objects.nonNull(categoryDTO.getTitle()) &&
                Objects.nonNull(categoryDTO.getDescription()) &&
                Objects.nonNull(categoryDTO.getGroup())) {
            return;
        }
        throw new ExpenseTrackerBadRequestException(ExpenseError.BAD_REQUEST.getDescription(), ExpenseError.BAD_REQUEST);
    }

    @Override
    public void validateForRetrieve(CategoryDTO categoryDTO) {
        if(Objects.nonNull(categoryDTO) && Objects.nonNull(categoryDTO.getCategoryId())) {
            return;
        }
        throw new ExpenseTrackerBadRequestException(ExpenseError.BAD_REQUEST.getDescription(), ExpenseError.BAD_REQUEST);
    }

    @Override
    public void validateForDelete(CategoryDTO categoryDTO) {
        if(Objects.nonNull(categoryDTO) && Objects.nonNull(categoryDTO.getCategoryId())) {
            return;
        }
        throw new ExpenseTrackerBadRequestException(ExpenseError.BAD_REQUEST.getDescription(), ExpenseError.BAD_REQUEST);
    }
}
