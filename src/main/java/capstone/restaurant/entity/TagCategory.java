package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "TAG_CAT_SEQ_GEN" , sequenceName = "TAG_CAT_SEQUENCE")
public class TagCategory {
    @Id
    @GeneratedValue(generator = "TAG_CAT_SEQUENCE")
    private Long id;

    private String categoryName;

    @Builder.Default
    @OneToMany(mappedBy = "tagCategory")
    private List<Tag> tags = new ArrayList<>();
}
