package capstone.restaurant.vote.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMemberReturnDto {

    public GetMemberReturnDto(String member, Integer age) {
        this.member = member;
        this.age = age;
    }

    private String member;
    private Integer age;
}
