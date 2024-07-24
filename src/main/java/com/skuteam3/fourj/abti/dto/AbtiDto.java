package com.skuteam3.fourj.abti.dto;

import com.skuteam3.fourj.abti.domain.Abti;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AbtiDto {
    private Long id;
    private String title;
    private String description;
    private String detail;
    private String improvingDescription1;
    private String improvingDescription2;
    private String improvingDescription3;

    public static Abti toEntity(AbtiDto abtiDto) {
        return Abti.builder()
                .title(abtiDto.getTitle())
                .description(abtiDto.getDescription())
                .detail(abtiDto.getDetail())
                .improvingDescription1(abtiDto.getImprovingDescription1())
                .improvingDescription2(abtiDto.getImprovingDescription2())
                .improvingDescription3(abtiDto.getImprovingDescription3())
                .build();
    }

    public static AbtiDto of (Abti abti) {
        return AbtiDto.builder()
                .id(abti.getId())
                .title(abti.getTitle())
                .description(abti.getDescription())
                .detail(abti.getDetail())
                .improvingDescription1(abti.getImprovingDescription1())
                .improvingDescription2(abti.getImprovingDescription2())
                .improvingDescription3(abti.getImprovingDescription3())
                .build();
    }
}
