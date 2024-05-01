package capstone.restaurant.repository;

import capstone.restaurant.entity.Restaurant;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RestaurantRepositoryImpl(JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Restaurant> findRestaurantListByTagOrKeyWord() {
        return null;
    }
}
