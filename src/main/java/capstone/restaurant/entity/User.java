package capstone.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "USER_SEQ_GEN" , sequenceName = "USER_SEQUENCE")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "USER_SEQUENCE")
    private Long id;

    private String nickname;

    private String providerId;

    private int profileImage;
}
