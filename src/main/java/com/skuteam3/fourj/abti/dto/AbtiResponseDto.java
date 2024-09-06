package com.skuteam3.fourj.abti.dto;

import com.skuteam3.fourj.abti.domain.Abti;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AbtiResponseDto {
    @Schema(description = "Abti ID", example="1")
    private Long id;
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
    @Schema(description="이미지 경로", example="http://이미지경로")
    private String image;

    public static Abti toEntity(AbtiResponseDto abtiResponseDto) {
        return Abti.builder()
                .title(abtiResponseDto.getTitle())
                .description(abtiResponseDto.getDescription())
                .detail(abtiResponseDto.getDetail())
                .improvingDescription1(abtiResponseDto.getImprovingDescription1())
                .improvingDescription2(abtiResponseDto.getImprovingDescription2())
                .improvingDescription3(abtiResponseDto.getImprovingDescription3())
                .image(abtiResponseDto.getImage())
                .build();
    }

    public static AbtiResponseDto of (Abti abti) {
        return AbtiResponseDto.builder()
                .id(abti.getId())
                .title(abti.getTitle())
                .description(abti.getDescription())
                .detail(abti.getDetail())
                .improvingDescription1(abti.getImprovingDescription1())
                .improvingDescription2(abti.getImprovingDescription2())
                .improvingDescription3(abti.getImprovingDescription3())
                .image(abti.getImage())
                .build();
    }

    @Override
    public String toString() {
        return "AbtiResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", detail='" + detail + '\'' +
                ", improvingDescription1='" + improvingDescription1 + '\'' +
                ", improvingDescription2='" + improvingDescription2 + '\'' +
                ", improvingDescription3='" + improvingDescription3 + '\'' +
                '}';
    }
}
