package com.phoenix.expensetrackerservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockedCategoryDetails {

    private String categoryId;
    private String username;
    private String title;
}
