package capstone.restaurant.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "USER_SEQUENCE")
    private Long id;

    private String nickname;

    private String providerId;

    private int profileImage;
    @NotNull
    @Schema(example = "동창회 식당 정하기")
    private String token;
}
