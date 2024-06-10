package capstone.restaurant.entity;


import capstone.restaurant.dto.tag.TagResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@SequenceGenerator(name = "TAG_SEQ_GEN" , sequenceName = "TAG_SEQUENCE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(generator = "TAG_SEQ_GEN")
    private long id;

    private String tagName;

    @ManyToOne
    @JoinColumn(name="tag_category_id")
    private TagCategory tagCategory;

    public static TagResponse toDto(Tag tag) {
        return new TagResponse(tag.getTagName(), tag.getTagCategory().getCategoryName());
    }

//    public TagResponse toDtoList(List<Tag> tag) {
//        return new TagResponse(tag.getTagName(), tag.getTagCategory().getCategoryName());
//    }
}
