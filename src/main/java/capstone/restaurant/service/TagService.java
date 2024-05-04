package capstone.restaurant.service;

import capstone.restaurant.dto.tag.TagListResponse;
import capstone.restaurant.dto.tag.TagResponse;
import capstone.restaurant.entity.Tag;
import capstone.restaurant.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    public TagListResponse getTags(){
        List<Tag> tags = tagRepository.findAll();

        List<TagResponse> tagResponseList =  tags.stream().map(tag -> {
            return new TagResponse(tag.getTagName(), tag.getTagCategory().getCategoryName());
        }).toList();

        return new TagListResponse(tagResponseList);
    }
}
