package com.skuteam3.fourj.abti.dto;

import com.skuteam3.fourj.abti.domain.Abti;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description ="Abti")
@Builder
@Data
public class AbtiRequestDto {

    @Schema(description="Abti 제목", example="번개 술꾼")
    private String title;
    @Schema(description="Abti 설명", example="무계획으로 혼자 많은 양의 술을 즐기는 사람")
    private String description;
    @Schema(description="Abti 추가 문구", example="즉흥의 마법사")
    private String detail;
    @Schema(description="음주 습관 개선 1", example="즉흥적인 음주가 즐거울 수 있지만 계획되지 않은 음주는 건강에 해로울 수 있습니다.")
    private String improvingDescription1;
    @Schema(description="음주 습관 개선 2", example="미리 계획된 술자리를 가지면서 자신을 돌보세요.")
    private String improvingDescription2;
    @Schema(description="음주 습관 개선 3", example="간헐적인 술 없는 날을 정해 건강을 지키세요.")
    private String improvingDescription3;

    public static Abti toEntity(AbtiRequestDto abtiRequestDto) {
        return Abti.builder()
                .title(abtiRequestDto.getTitle())
                .description(abtiRequestDto.getDescription())
                .detail(abtiRequestDto.getDetail())
                .improvingDescription1(abtiRequestDto.getImprovingDescription1())
                .improvingDescription2(abtiRequestDto.getImprovingDescription2())
                .improvingDescription3(abtiRequestDto.getImprovingDescription3())
                .build();
    }

    public static AbtiRequestDto of (Abti abti) {
        return AbtiRequestDto.builder()
                .title(abti.getTitle())
                .description(abti.getDescription())
                .detail(abti.getDetail())
                .improvingDescription1(abti.getImprovingDescription1())
                .improvingDescription2(abti.getImprovingDescription2())
                .improvingDescription3(abti.getImprovingDescription3())
                .build();
    }

    @Override
    public String toString() {
        return "AbtiResponseDto{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", detail='" + detail + '\'' +
                ", improvingDescription1='" + improvingDescription1 + '\'' +
                ", improvingDescription2='" + improvingDescription2 + '\'' +
                ", improvingDescription3='" + improvingDescription3 + '\'' +
                '}';
    }
}
