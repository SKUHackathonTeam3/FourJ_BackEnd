package com.skuteam3.fourj.mission.controller;

import com.skuteam3.fourj.jwt.provider.JwtProvider;
import com.skuteam3.fourj.mission.dto.MissionDto;
import com.skuteam3.fourj.mission.service.MissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
@RestController
public class MissionController {

    private final MissionService missionService;
    private final JwtProvider jwtProvider;

    // Get all missions
    @GetMapping
    public ResponseEntity<?> getAllMissions() {

        try {

            return ResponseEntity.ok(missionService.getAllMissions());
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to get missions: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get missions");
        }
    }


    @PostMapping("/{missionId}/clear")
    public ResponseEntity<?> clearMissions(HttpServletRequest request, @PathVariable Long missionId) {

        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        try {

            missionService.clearMission(missionId, userEmail);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to clear mission: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to clear mission");
        }

        return ResponseEntity.ok("Mission cleared");
    }

    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedMissions(HttpServletRequest request) {
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        try {

            return ResponseEntity.ok(missionService.getUserCompletedMissions(userEmail));
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body("Failed to get completed missions: " + rse.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get completed missions");
        }
    }




}
