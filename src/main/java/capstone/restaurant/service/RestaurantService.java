package capstone.restaurant.service;

import capstone.restaurant.dto.restaurant.RestaurantListResponse;
import capstone.restaurant.dto.restaurant.RestaurantListSub;
import capstone.restaurant.dto.tag.TagResponse;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.RestaurantTag;
import capstone.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantListResponse restaurantListFindByTag(String place , String[] tags , int page){

        RestaurantListResponse response = new RestaurantListResponse();
        response.setRestaurants(this.restaurantRepository.findRestaurantListByTag(place , tags , page));
        return response;
    }

    @Transactional
    public RestaurantListResponse restaurantListResponseByKeyword(String keyword , Integer page){
        RestaurantListResponse response = new RestaurantListResponse();

        Page<Restaurant> restaurantList = restaurantRepository.findRestaurantsByNameContaining(keyword, PageRequest.of(page - 1 , 2));
        response.setRestaurants(convertEntitiesToDto(restaurantList));
        return response;
    }

    private List<RestaurantListSub> convertEntitiesToDto(Page<Restaurant> restaurantList){

        List<RestaurantListSub> restaurantListSubs = new ArrayList<RestaurantListSub>();
        List<TagResponse> tagResponseList = new ArrayList<TagResponse>();

        for(Restaurant restaurant : restaurantList){
            RestaurantListSub restaurantListSub = new RestaurantListSub();
            restaurantListSub.setName(restaurant.getName());
            restaurantListSub.setThumbnail(restaurant.getThumbnail());
            restaurantListSub.setRestaurantId(restaurant.getRestaurantHash());

            for (RestaurantTag restaurantTag : restaurant.getRestaurantTag()) {
                tagResponseList.add(new TagResponse(restaurantTag.getTag().getTagName() , restaurantTag.getTag().getTagCategory().getCategoryName()));
            }

            restaurantListSub.setTags(tagResponseList);
            restaurantListSubs.add(restaurantListSub);
        }
        return restaurantListSubs;
    }
}
