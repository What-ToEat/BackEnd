package capstone.restaurant.controller;

import capstone.restaurant.entity.Tag;
import capstone.restaurant.entity.TagCategory;
import capstone.restaurant.repository.CategoryRepository;
import capstone.restaurant.repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TagControllerTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void test() throws Exception {
        // given
        TagCategory tagCategory = TagCategory.builder().categoryName("category").build();
        categoryRepository.save(tagCategory);
        Tag tag = Tag.builder().tagName("tag").tagCategory(tagCategory).build();
        tagRepository.save(tag);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                .get("/api/tags")
                .accept(MediaType.ALL));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tags[0].name").value("tag"))
                .andExpect(jsonPath("$.data.tags[0].category").value("category"));
    }
}