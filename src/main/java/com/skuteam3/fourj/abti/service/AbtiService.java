package com.skuteam3.fourj.abti.service;

import com.skuteam3.fourj.abti.domain.Abti;
import com.skuteam3.fourj.abti.dto.AbtiDto;
import com.skuteam3.fourj.abti.repository.AbtiRepository;
import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserInfoRepository;
import com.skuteam3.fourj.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AbtiService {

    private final AbtiRepository abtiRepository;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public AbtiDto getAbtiById(Long id) {

        Abti abti = abtiRepository.findById(id).orElse(null);

        if (abti == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Abti not found");
        }

        return AbtiDto.of(abti);
    }

    public List<AbtiDto> getAllAbti() {

        List<Abti> abtiList = abtiRepository.findAll();

        return abtiList.stream().map(AbtiDto::of).collect(Collectors.toList());
    }

    public void setUserInfoAbti(String userEmail, Long id) {

        User user = userRepository.findByEmail(userEmail).orElse(null);
        Abti abti = abtiRepository.findById(id).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (abti == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Abti not found");
        }

        UserInfo userInfo = user.getUserInfo();
        userInfo.setAbti(abti);

        userInfoRepository.save(userInfo);
    }

    public Long save(AbtiDto abtiDto) {

        Abti abti = abtiRepository.save(AbtiDto.toEntity(abtiDto));

        return abti.getId();
    }

    public void updateAbtiById(Long id, AbtiDto dto) {

        Abti abti = abtiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abti not found"));

        if (dto.getTitle() != null) abti.setTitle(dto.getTitle());
        if (dto.getDetail() != null) abti.setDetail(dto.getDetail());
        if (dto.getDescription() != null) abti.setDescription(dto.getDescription());
        if (dto.getImprovingDescription1()!= null) abti.setImprovingDescription1(dto.getImprovingDescription1());
        if (dto.getImprovingDescription2()!= null) abti.setImprovingDescription2(dto.getImprovingDescription2());
        if (dto.getImprovingDescription3()!= null) abti.setImprovingDescription3(dto.getImprovingDescription3());

        abtiRepository.save(abti);
    }

    public void delete(Long id) {

        Abti abti = abtiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Abti not found"));

        List<UserInfo> userInfoList = userInfoRepository.findByAbti(abti);
        userInfoList.forEach(userInfo -> userInfo.setAbti(null));

        abtiRepository.delete(abti);
    }
}
