package capstone.restaurant.voteTest;

import capstone.restaurant.vote.VoteController;
import capstone.restaurant.vote.dto.GetMemberReturnDto;
import capstone.restaurant.vote.dto.ResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class voteExceptionTest {

    @Autowired
    private VoteController voteController;

    @Test
    public void testReturnValue_Success(){

        ResponseDto<GetMemberReturnDto> responseDto = new ResponseDto<>(201 , "CREATED" , new GetMemberReturnDto("132" , 1));
        Assertions.assertThat(voteController.getMember("123", 1).getBody().getStatusCode()).isEqualTo(201);
    }

    @Test
    public void testReturnValue_Fail(){



        Assertions.assertThatThrownBy(() -> { voteController.getMember("123" , 2);}).isInstanceOf(EntityNotFoundException.class);
    }
}
