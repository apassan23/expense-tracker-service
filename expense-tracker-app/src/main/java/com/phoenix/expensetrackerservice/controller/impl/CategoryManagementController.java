package com.phoenix.expensetrackerservice.controller.impl;

import com.phoenix.expensetrackerservice.annotation.Log;
import com.phoenix.expensetrackerservice.constants.ControllerConstants;
import com.phoenix.expensetrackerservice.constants.LogConstants;
import com.phoenix.expensetrackerservice.controller.CategoryManagementAPI;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.service.CategoryManagementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING)
public class CategoryManagementController implements CategoryManagementAPI {
    private final CategoryManagementService categoryManagementService;

    public CategoryManagementController(CategoryManagementService categoryManagementService) {
        this.categoryManagementService = categoryManagementService;
    }

    @Override
    @Log(action = LogConstants.CREATE_ACTION)
    @PostMapping(path = ControllerConstants.CATEGORY_CREATE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO response = categoryManagementService.createCategory(categoryDTO);
        return new ResponseEntity<>(response, getResponseHeaders(), HttpStatus.CREATED);
    }

    @Override
    @Log(action = LogConstants.RETRIEVE_ACTION)
    @GetMapping(path = ControllerConstants.CATEGORY_RETRIEVE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> retrieveCategory(@PathVariable String categoryId) {
        CategoryDTO response = categoryManagementService.retrieveCategory(categoryId);
        return ResponseEntity.ok().headers(getResponseHeaders()).body(response);
    }

    @Override
    @Log(action = LogConstants.RETRIEVE_ALL_ACTION)
    @GetMapping(path = ControllerConstants.CATEGORY_RETRIEVE_ALL_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> retrieveCategories() {
        List<CategoryDTO> categories = categoryManagementService.retrieveCategories();
        return ResponseEntity.ok().headers(getResponseHeaders()).body(categories);
    }

    @Override
    @Log(action = LogConstants.CHANGE_ACTION)
    @PutMapping(path = ControllerConstants.CATEGORY_CHANGE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> changeCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO response = categoryManagementService.changeCategory(categoryDTO);
        return ResponseEntity.ok().headers(getResponseHeaders()).body(response);
    }

    @Override
    @Log(action = LogConstants.DELETE_ACTION)
    @DeleteMapping(path = ControllerConstants.CATEGORY_DELETE_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId) {
        categoryManagementService.deleteCategory(categoryId);
        return ResponseEntity.accepted().headers(getResponseHeaders()).build();
    }

    private HttpHeaders getResponseHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
