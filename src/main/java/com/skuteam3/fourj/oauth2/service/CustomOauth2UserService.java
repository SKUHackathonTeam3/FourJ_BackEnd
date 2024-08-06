package com.skuteam3.fourj.oauth2.service;

import com.skuteam3.fourj.account.Role;
import com.skuteam3.fourj.account.domain.CustomUserDetails;
import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserInfoRepository;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.oauth2.SocialType;
import com.skuteam3.fourj.oauth2.domain.SocialUser;
import com.skuteam3.fourj.oauth2.repository.SocialUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final SocialUserRepository socialUserRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String oauthClientName = oAuth2UserRequest.getClientRegistration().getClientName();

        SocialUser socialUser = null;
        String userId = "";
        String userName = "";
        String userEmail = "";

        if (oauthClientName.equals("naver")) {
            LinkedHashMap attributes = (LinkedHashMap) oAuth2User.getAttributes().get("response");

            userId = ((String) attributes.get("id")).substring(0, ((String)attributes.get("id")).length() - 1);
            userName = (String) attributes.get("name");
            userEmail = (String) attributes.get("email");
            socialUser = SocialUser.builder().email(userEmail).socialType(SocialType.NAVER).providerId(userId).build();
        }
        else if (oauthClientName.equals("kakao")) {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            userId = ((Long) attributes.get("id")).toString();
            LinkedHashMap linkedHashMap = (LinkedHashMap) attributes.get("properties");
            userName = (String) linkedHashMap.get("nickname");
            linkedHashMap = (LinkedHashMap) attributes.get("kakao_account");
            userEmail = (String) linkedHashMap.get("email");

            socialUser = SocialUser.builder().email(userEmail).socialType(SocialType.KAKAO).providerId(userId).build();
        }

        else if(oauthClientName.equals("google")) {
            userId = oAuth2User.getAttribute("sub");
            userEmail = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("name");
            socialUser = SocialUser.builder().email(userEmail).socialType(SocialType.GOOGLE).providerId(userId).build();
        }
        else {
            throw new IllegalArgumentException("Unsupported social client name: " + oauthClientName);
        }

        if (socialUser == null) {
            throw new IllegalStateException("Failed to find or create social user.");
        }

        List<SocialUser> existingUser = socialUserRepository.findByEmail(socialUser.getEmail());
        for (SocialUser existingSocialUser: existingUser) {
            if (existingSocialUser.getSocialType().equals(socialUser.getSocialType())) {
                return new CustomUserDetails(existingSocialUser.getUser(), oAuth2User.getAttributes());
            }
        }

        User user = userRepository.findByEmail(userEmail).orElse(null);

        if (user == null) {

            UserInfo userInfo = UserInfo.builder()
                    .name(userName)
                    .build();

            userInfoRepository.save(userInfo);

            user = User.builder()
                    .name(userName)
                    .email(userEmail)
                    .userRole(Role.USER)
                    .userInfo(userInfo)
                    .build();
        }

        socialUser.setUser(user);

        if (user.getUserId() == null) {
            userRepository.save(user);
        }

        socialUserRepository.save(socialUser);

        return new CustomUserDetails(socialUser.getUser(), oAuth2User.getAttributes());
    }
}
