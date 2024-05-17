package capstone.restaurant.interceptor;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.stream.Stream;

@Slf4j
@Component
public class LoggerInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler , Exception ex) throws Exception {

        ContentCachingRequestWrapper requestInfo = (ContentCachingRequestWrapper) request;
        String requestBody = requestInfo.getContentAsString();

        if(200 <= response.getStatus() && response.getStatus() < 300){
            log.info("url = {} , method = {} , body = {}" , request.getRequestURL() , request.getMethod() , requestBody);
        }else{
            log.error("url = {} , method = {} , status = {} ,  body = {}" , request.getRequestURL() , request.getMethod() , response.getStatus() , requestBody);
        }
    }
}

