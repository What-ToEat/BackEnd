package capstone.restaurant.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "RES_TAG_SEQ_GEN" , sequenceName = "RES_TAG_SEQ")
@Table(indexes = @Index(name = "idx_tag" , columnList = "tag_id"))
public class RestaurantTag {

    @Id
    @GeneratedValue(generator = "RES_TAG_SEQ_GEN")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
