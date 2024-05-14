package capstone.restaurant.repository;

import capstone.restaurant.dto.restaurant.RestaurantListSub;
import capstone.restaurant.dto.tag.TagResponse;
import capstone.restaurant.entity.Restaurant;
import capstone.restaurant.entity.RestaurantTag;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static capstone.restaurant.entity.QRestaurantTag.restaurantTag;

public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RestaurantRepositoryImpl(JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

    @Override
    public List<RestaurantListSub> findRestaurantListByTag(String place , String[] tags , int page) {

        int unit = 10;

        BooleanExpression placeCondition = place != null ? restaurantTag.restaurant.place.eq(place) : null;
        BooleanExpression tagWhereCondition = tags != null ? restaurantTag.tag.tagName.in(tags) : null;
        BooleanExpression tagHavingCondition = tags != null ? restaurantTag.restaurant.count().eq((long) tags.length) : null;

        List<Restaurant> query = queryFactory.select(restaurantTag.restaurant)
                .from(restaurantTag)
                .where(tagWhereCondition)
                .where(placeCondition)
                .groupBy(restaurantTag.restaurant)
                .having(tagHavingCondition)
                .offset((long) (page - 1) * unit)
                .limit(unit)
                .fetch();

        List<RestaurantListSub> result = new ArrayList<RestaurantListSub>();

        for (Restaurant item : query){
            List<TagResponse> tagResponseList = new ArrayList<>();

            for(RestaurantTag tag: item.getRestaurantTag()){
                tagResponseList.add(new TagResponse(tag.getTag().getTagName() , tag.getTag().getTagCategory().getCategoryName()));
            }

            RestaurantListSub restaurantListSub = new RestaurantListSub();
            restaurantListSub.setName(item.getName());
            restaurantListSub.setThumbnail(item.getThumbnail());
            restaurantListSub.setTags(tagResponseList);
            restaurantListSub.setRestaurantId(item.getRestaurantHash());
            result.add(restaurantListSub);
        }

        return result;
    }
}
