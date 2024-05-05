package capstone.restaurant.service;

import capstone.restaurant.dto.restaurant.RestaurantListResponse;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public RestaurantListResponse restaurantListResponseByKeyword(String keyword , Integer page){
        RestaurantListResponse response = new RestaurantListResponse();
        restaurantRepository.findRestaurantsByNameContaining(keyword , PageRequest.of(5 , page - 1));

        return response;
    }
}
