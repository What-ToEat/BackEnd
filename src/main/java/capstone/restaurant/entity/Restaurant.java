package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@SequenceGenerator(
        name= "RESTAURANT_SEQ_GEN",
        sequenceName = "RESTAURANT_SEQ")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTAURANT_SEQ_GEN")
    private Long id;

    @Column(unique = true)
    private String restaurantHash;

    private String address;

    private String name;

}
