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
            Object response = oAuth2User.getAttributes().get("response");
            if (response instanceof Map<?,?> responseMap) {
                userId = ((String) responseMap.get("id")).substring(0, ((String)responseMap.get("id")).length() - 1);
                userName = (String) responseMap.get("name");
                userEmail = (String) responseMap.get("email");
                socialUser = SocialUser.builder().email(userEmail).socialType(SocialType.NAVER).providerId(userId).build();
            }
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

        Optional<SocialUser> existingUser = socialUserRepository.findByEmail(socialUser.getEmail());
        if (existingUser.isPresent() && existingUser.get().getSocialType().equals(socialUser.getSocialType())) {
            return new CustomUserDetails(socialUser.getUser(), oAuth2User.getAttributes());
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
