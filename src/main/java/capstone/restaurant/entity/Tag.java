package capstone.restaurant.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "TAG_SEQ_GEN" , sequenceName = "TAG_SEQUENCE")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tagName;

    @ManyToOne
    @JoinColumn(name="tag_category_id")
    private TagCategory tagCategory;

}
