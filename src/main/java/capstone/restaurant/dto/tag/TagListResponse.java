package capstone.restaurant.dto.tag;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagListResponse {
    private List<String> tags;
}
