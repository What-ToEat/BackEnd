package capstone.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(
        name= "VOTE_SEQ_GEN",
        sequenceName = "VOTE_SEQ")
public class Vote {

    @Id
    @GeneratedValue(generator = "VOTE_SEQ_GEN")
    private Long id;

    private String voteHash;
}
