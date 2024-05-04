package capstone.restaurant.repository;

import capstone.restaurant.dto.restaurant.RestaurantListSub;
import capstone.restaurant.dto.tag.TagResponse;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.RestaurantTag;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static capstone.restaurant.entity.QRestaurantTag.restaurantTag;

public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RestaurantRepositoryImpl(JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

    @Override
    public List<RestaurantListSub> findRestaurantListByTagOrKeyWord(String place , String[] tags , int page) {

        List<Restaurant> query = queryFactory.select(restaurantTag.restaurant)
                .from(restaurantTag)
                .where(restaurantTag.tag.tagName.in(tags))
                .groupBy(restaurantTag.restaurant)
                .having(restaurantTag.restaurant.count().eq((long) tags.length))
                .offset(page)
                .limit(5)
                .fetch();

        List<RestaurantListSub> result = new ArrayList<RestaurantListSub>();

        for (Restaurant item : query){
            List<TagResponse> tagResponseList = new ArrayList<>();

            for(RestaurantTag tag: item.getRestaurantTag()){
                tagResponseList.add(new TagResponse(tag.getTag().getTagName() , "양식"));
            }

            RestaurantListSub rls = new RestaurantListSub();
            rls.setName(item.getName());
            rls.setThumbnail(item.getAddress());
            rls.setTags(tagResponseList);
            rls.setRestaurantId(item.getRestaurantHash());
            result.add(rls);
        }


        return result;
    }
}
