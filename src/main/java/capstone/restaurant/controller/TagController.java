package capstone.restaurant.controller;

import capstone.restaurant.dto.ResponseDto;
import capstone.restaurant.dto.tag.TagListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "tags", description = "태그 조회 API")
@RestController
@RequestMapping("api/tags")
public class TagController {
    @Operation(summary = "태그 목록 조회", description = "모든 태그 정보를 조회한다.")
    @GetMapping
    public ResponseDto<TagListResponse> getTagList() {
        return new ResponseDto<>(200, "ok", new TagListResponse());
    }
}
