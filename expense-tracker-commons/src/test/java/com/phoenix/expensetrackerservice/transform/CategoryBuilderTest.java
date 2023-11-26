package com.phoenix.expensetrackerservice.transform;

import com.phoenix.expensetrackerservice.model.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class CategoryBuilderTest {

    @Test
    public void testBuildFromCategoryId() {
        String categoryId = "dummy";

        CategoryDTO categoryDTO = CategoryBuilder.buildFromCategoryId(categoryId);
        assertNotNull(categoryDTO);
        assertEquals(categoryId, categoryDTO.getCategoryId());
        assertNull(categoryDTO.getDescription());
        assertNull(categoryDTO.getGroup());
        assertNull(categoryDTO.getTitle());
    }
}
