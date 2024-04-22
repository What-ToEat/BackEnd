package capstone.restaurant.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private LoggerInterceptor logger;

    public WebConfig(LoggerInterceptor loggerInterceptor){
        this.logger = loggerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.logger).addPathPatterns("/**");
    }
}
