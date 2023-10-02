package com.phoenix.expensetrackerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("categories")
public class Category {
    @Id
    private String categoryId;

    private String title;
    private String description;
    private String group;
}
