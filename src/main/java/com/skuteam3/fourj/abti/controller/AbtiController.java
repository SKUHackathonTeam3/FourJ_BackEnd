package com.skuteam3.fourj.abti.controller;

import com.skuteam3.fourj.abti.dto.AbtiDto;
import com.skuteam3.fourj.abti.service.AbtiService;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AbtiController {

    private final AbtiService abtiService;
    private final JwtProvider jwtProvider;

    // Abti 생성
    @PostMapping("/abtis")
    public ResponseEntity<?> createAbti(@RequestBody AbtiDto dto) {

        try {

            Long id = abtiService.save(dto);

            return ResponseEntity.created(URI.create("/api/v1/abtis/" + id)).build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to create Abti: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to create Abti");
        }
    }

    // 모든 Abti 조회
    @GetMapping("/abtis")
    public ResponseEntity<?> getAllAbtis() {
        try {

            List<AbtiDto> abtiDtoList = abtiService.getAllAbti();

            return ResponseEntity.ok(abtiDtoList);
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to get Abtis");
        }
    }

    // Abti Id값으로 Abti 조회
    @GetMapping("/abtis/{id}")
    public ResponseEntity<?> getAbtiById(@PathVariable Long id) {

        try {

            AbtiDto abtiDto = abtiService.getAbtiById(id);

            return ResponseEntity.ok(abtiDto);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to get Abti: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to get Abti");
        }
    }

    // Abti 수정
    @PatchMapping("/abtis/{id}")
    public ResponseEntity<?> updateAbti(@PathVariable Long id, @RequestBody AbtiDto dto) {
        try {
            abtiService.updateAbtiById(id, dto);

            return ResponseEntity.ok().build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to update Abti: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to update Abti");
        }
    }

    // Abti 삭제
    @DeleteMapping("/abtis/{id}")
    public ResponseEntity<?> deleteAbti(@PathVariable Long id) {

        try {

            abtiService.delete(id);

            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to delete Abti: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to delete Abti");
        }
    }

    // 유저 인포에 Abti 추가 || 수정
    @PatchMapping("/abtis/userinfos/{abtiId}")
    public ResponseEntity<?> updateUserInfoAbti(HttpServletRequest request, @RequestBody Long abtiId) {

        try {

            String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

            abtiService.setUserInfoAbti(userEmail, abtiId);

            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to update Abti: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to update Abti");
        }
    }



}
