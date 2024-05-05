package capstone.restaurant.RestaurantTest;

import capstone.restaurant.dto.restaurant.RestaurantListResponse;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.RestaurantTag;
import capstone.restaurant.entity.Tag;
import capstone.restaurant.entity.TagCategory;
import capstone.restaurant.repository.RestaurantRepository;
import capstone.restaurant.repository.RestaurantTagRepository;
import capstone.restaurant.repository.TagCategoryRepository;
import capstone.restaurant.repository.TagRepository;
import capstone.restaurant.service.RestaurantService;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class RestaurantTest {

    @Autowired
    public RestaurantRepository restaurantRepository;
    @Autowired
    public RestaurantTagRepository restaurantTagRepository;
    @Autowired
    public TagRepository tagRepository;
    @Autowired
    public RestaurantService restaurantService;
    @Autowired
    public TagCategoryRepository tagCategoryRepository;


    @AfterEach
    public void afterEach(){
        restaurantTagRepository.deleteAll();
        restaurantRepository.deleteAll();
        tagRepository.deleteAll();
        tagCategoryRepository.deleteAll();
    }

    @Test
    public void test_tag_query(){
        Restaurant restaurant1 = Restaurant.builder().name("abc").restaurantHash("13").build();
        Restaurant restaurant2 = Restaurant.builder().name("def").restaurantHash("12").build();

        TagCategory tagCategory = TagCategory.builder().categoryName("양식").build();

        Tag tag1 = Tag.builder().tagName("맛있어요").tagCategory(tagCategory).build();
        Tag tag2 = Tag.builder().tagName("분위기가 좋아요").tagCategory(tagCategory).build();
        Tag tag3 = Tag.builder().tagName("넓어요").tagCategory(tagCategory).build();

        RestaurantTag rt1 = new RestaurantTag();
        rt1.setRestaurant(restaurant1);
        rt1.setTag(tag1);

        RestaurantTag rt2 = new RestaurantTag();
        rt2.setRestaurant(restaurant1);
        rt2.setTag(tag2);

        RestaurantTag rt3 = new RestaurantTag();
        rt3.setRestaurant(restaurant2);
        rt3.setTag(tag1);

        RestaurantTag rt4 = new RestaurantTag();
        rt4.setRestaurant(restaurant2);
        rt4.setTag(tag3);

        Restaurant[] restaurants = {restaurant1 , restaurant2};
        Tag[] tags = {tag1 , tag2 , tag3};
        RestaurantTag[] restaurantTags = {rt1 , rt2 , rt3 , rt4};

        restaurantRepository.saveAll(Arrays.asList(restaurants));
        tagCategoryRepository.save(tagCategory);
        tagRepository.saveAll(Arrays.asList(tags));
        restaurantTagRepository.saveAll(Arrays.asList(restaurantTags));

        String[] list  = {"맛있어요" , "넓어요"};

        RestaurantListResponse response = restaurantService.restaurantListFindByTag("", list, 1);

        Assertions.assertThat(response.getRestaurants().get(0).getRestaurantId()).isEqualTo("12");

    }

    @Test
    public void test_keyword_query(){
        Restaurant restaurant1 = Restaurant.builder().name("abc").restaurantHash("13").build();
        Restaurant restaurant2 = Restaurant.builder().name("abd").restaurantHash("12").build();
        Restaurant restaurant3 = Restaurant.builder().name("cde").restaurantHash("23").build();
        Restaurant restaurant4 = Restaurant.builder().name("abd").restaurantHash("53").build();
        Restaurant[] re = {restaurant1 , restaurant2 , restaurant3 , restaurant4};

        restaurantRepository.saveAll(Arrays.asList(re));

        RestaurantListResponse response1 = restaurantService.restaurantListResponseByKeyword("ab", 1);
        RestaurantListResponse response2 = restaurantService.restaurantListResponseByKeyword("ab", 2);

        Assertions.assertThat(response1.getRestaurants().size()).isEqualTo(2);
        Assertions.assertThat(response2.getRestaurants().size()).isEqualTo(1);


    }
}
