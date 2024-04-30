package capstone.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMemberReturnDto {

    private String member;
    private Integer age;

    public GetMemberReturnDto(String member, Integer age) {
        this.member = member;
        this.age = age;
    }
}
