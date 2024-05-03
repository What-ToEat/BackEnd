package capstone.restaurant.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TagListResponse {
    private List<TagResponse> tags;
}
