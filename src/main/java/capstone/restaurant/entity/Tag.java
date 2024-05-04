package capstone.restaurant.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "TAG_SEQ_GEN" , sequenceName = "TAG_SEQUENCE")
public class Tag {

    @Id
    @GeneratedValue(generator = "TAG_SEQ_GEN")
    private long id;

    private String tagName;

    @ManyToOne
    @JoinColumn(name="tag_category_id")
    private TagCategory tagCategory;

}
