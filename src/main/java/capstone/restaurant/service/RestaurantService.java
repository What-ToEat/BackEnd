package capstone.restaurant.service;

import capstone.restaurant.dto.restaurant.RestaurantListResponse;
import capstone.restaurant.dto.restaurant.RestaurantListSub;
import capstone.restaurant.dto.restaurant.RestaurantResponse;
import capstone.restaurant.dto.restaurant.ReviewListSub;
import capstone.restaurant.dto.tag.TagResponse;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.RestaurantTag;
import capstone.restaurant.entity.Review;
import capstone.restaurant.entity.Tag;
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
    public RestaurantListResponse restaurantListFindByKeyword(String keyword , Integer page){
        RestaurantListResponse response = new RestaurantListResponse();

        Page<Restaurant> restaurantList = this.restaurantRepository.findRestaurantsByNameContaining(keyword, PageRequest.of(page - 1 , 2));
        response.setRestaurants(convertEntitiesToDto1(restaurantList));
        return response;
    }

    private List<RestaurantListSub> convertEntitiesToDto1(Page<Restaurant> restaurantList){

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

    public RestaurantResponse restaurantFindById(String restaurantId) {

        Restaurant restaurant = this.restaurantRepository.findRestaurantByRestaurantHash(restaurantId);
        return convertEntitiesToDto2(restaurant);
    }

    private RestaurantResponse convertEntitiesToDto2(Restaurant restaurant){
        List<TagResponse> tagResponseList = new ArrayList<TagResponse>();

        for (RestaurantTag restaurantTag : restaurant.getRestaurantTag()) {
            tagResponseList.add(new TagResponse(restaurantTag.getTag().getTagName() , restaurantTag.getTag().getTagCategory().getCategoryName()));
        }

        List<ReviewListSub> reviewList = new ArrayList<>();

        for (Review review : restaurant.getReviews()){
            reviewList.add(new ReviewListSub(review.getReview() , review.getIsAiReview()));
        }

        return new RestaurantResponse(restaurant.getName() , restaurant.getThumbnail() , tagResponseList , restaurant.getRestaurantHash() , reviewList);
    }
}
