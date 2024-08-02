package com.skuteam3.fourj.attendance.dto;

import lombok.Data;

@Data
public class AttendanceListRequestDto {

    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
}
