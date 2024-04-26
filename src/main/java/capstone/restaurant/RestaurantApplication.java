package capstone.restaurant;

import capstone.restaurant.entity.Restaurant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class RestaurantApplication {

	public static void main(String[] args) {
		// SpringApplication.run(RestaurantApplication.class, args);

		Restaurant restaurant = new Restaurant("seoul", "abc");

	}

}
