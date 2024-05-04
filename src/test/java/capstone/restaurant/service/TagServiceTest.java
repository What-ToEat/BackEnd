package capstone.restaurant.service;

import capstone.restaurant.dto.tag.TagListResponse;
import capstone.restaurant.entity.Tag;
import capstone.restaurant.entity.TagCategory;
import capstone.restaurant.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private TagService tagService;

    @Test
    @DisplayName("[getTags] 태그 전부 반환")
    void getTagsTest() {
        // Given
        List<Tag> tagList = new ArrayList<>();
        for ( int i = 0; i < 5; i++ ) {
            TagCategory tagCategory = TagCategory.builder().categoryName("category" + i).build();
            tagList.add(Tag.builder()
                    .tagName("name" + i)
                    .tagCategory(tagCategory)
                    .build()
            );
        }
        when(tagRepository.findAll()).thenReturn(tagList);

        //When
        TagListResponse tags = tagService.getTags();

        //Then
        assertThat(tags.getTags().get(0).getName()).isEqualTo("name0");
        assertThat(tags.getTags().get(0).getCategory()).isEqualTo("category0");
    }

    @Test
    @DisplayName("[getTags] 태그가 없는 경우 0개 반환")
    void getTagsTest2() {
        // Given
        List<Tag> tagList = new ArrayList<>();
        when(tagRepository.findAll()).thenReturn(tagList);

        //When
        TagListResponse tags = tagService.getTags();

        //Then
        assertThat(tags.getTags().size()).isEqualTo(0);
    }
}