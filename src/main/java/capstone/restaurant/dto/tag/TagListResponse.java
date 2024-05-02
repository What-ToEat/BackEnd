package capstone.restaurant.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagListResponse {
    private List<TagResponse> tags;
}
