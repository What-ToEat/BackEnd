package capstone.restaurant.service;

import capstone.restaurant.common.TokenProvider;
import capstone.restaurant.common.exception.InvalidTokenException;
import capstone.restaurant.dto.auth.TokenInfo;
import capstone.restaurant.dto.auth.Tokens;
import capstone.restaurant.entity.Member;
import capstone.restaurant.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    @Value("${spring.auth.kakao.appId}")
    private String appId;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public Tokens signupOrLogin(String token) {
        TokenInfo tokenInfo = verifyKakaoToken(token);
        Member member = memberRepository.findByProviderId(tokenInfo.getId());
        if (member == null) {
            member = signup(tokenInfo.getId());
        }
        String accessToken = tokenProvider.createAccessToken(member.getId());
        String refreshToken = tokenProvider.createRefreshToken(member.getId());
        return new Tokens(accessToken, refreshToken);
    }

     private Member signup(Long providerId) {
         Member member = Member.builder()
                 .providerId(providerId)
                 .build();
         return memberRepository.save(member);
    }


    private TokenInfo verifyKakaoToken(String token) {
        try {
            String url = "https://kapi.kakao.com/v1/user/access_token_info";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<TokenInfo> response = restTemplate.exchange(url, HttpMethod.GET, entity, TokenInfo.class);
             if (response.getBody().getAppId().toString().equals(appId)) {
                 return response.getBody();
             } else {
                 throw new InvalidTokenException("Invalid kakao token");
             }
        } catch (HttpClientErrorException e) {
            log.warn("Invalid kakao token");
            throw new IllegalArgumentException("Invalid kakao token");
        }
    }
}
