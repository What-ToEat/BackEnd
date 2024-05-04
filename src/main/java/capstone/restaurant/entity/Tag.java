package capstone.restaurant.entity;


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

}
