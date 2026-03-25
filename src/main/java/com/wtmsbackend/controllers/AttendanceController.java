package com.wtmsbackend.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.AttendanceRequest;
import com.wtmsbackend.dto.response.AttendanceResponse;
import com.wtmsbackend.services.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Attendance", description = "Endpoints for managing employee attendance records")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "Get all attendance records", description = "Retrieves a paginated list of all attendance records.")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAllAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<AttendanceResponse> attendancePage = attendanceService.getAllAttendance(page, size);

        ApiResponse<List<AttendanceResponse>> response = ApiResponse.<List<AttendanceResponse>>builder()
                .message("Attendance fetched successfully!")
                .success(true)
                .payload(attendancePage)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get attendance by Session ID", description = "Retrieves the attendance sheet for a specific training session.")
    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceBySession(
            @PathVariable Integer sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<AttendanceResponse> attendancePage = attendanceService.getAttendanceBySession(sessionId, page, size);

        ApiResponse<List<AttendanceResponse>> response = ApiResponse.<List<AttendanceResponse>>builder()
                .message("Session attendance fetched successfully!")
                .success(true)
                .payload(attendancePage)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get attendance by User ID", description = "Retrieves all attendance records for a specific employee.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceByUser(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<AttendanceResponse> attendancePage = attendanceService.getAttendanceByUser(userId, page, size);

        ApiResponse<List<AttendanceResponse>> response = ApiResponse.<List<AttendanceResponse>>builder()
                .message("User attendance fetched successfully!")
                .success(true)
                .payload(attendancePage)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get attendance by ID", description = "Retrieves a specific attendance record.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getAttendanceById(@PathVariable Integer id) {
        AttendanceResponse attendance = attendanceService.getAttendanceById(id);

        ApiResponse<AttendanceResponse> response = ApiResponse.<AttendanceResponse>builder()
                .message("Attendance record fetched successfully!")
                .success(true)
                .payload(attendance)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mark attendance", description = "Creates a new attendance record for an employee. Requires ADMIN or TRAINER role.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> createAttendance(@Valid @RequestBody AttendanceRequest request) {
        AttendanceResponse attendance = attendanceService.createAttendance(request);

        ApiResponse<AttendanceResponse> response = ApiResponse.<AttendanceResponse>builder()
                .message("Attendance marked successfully!")
                .success(true)
                .payload(attendance)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update attendance record", description = "Updates an existing attendance record. Requires ADMIN or TRAINER role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(
            @PathVariable Integer id,
            @Valid @RequestBody AttendanceRequest request) {
        AttendanceResponse attendance = attendanceService.updateAttendance(id, request);

        ApiResponse<AttendanceResponse> response = ApiResponse.<AttendanceResponse>builder()
                .message("Attendance updated successfully!")
                .success(true)
                .payload(attendance)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete attendance record", description = "Permanently deletes an attendance record. Requires ADMIN or TRAINER role.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Attendance record deleted successfully!")
                .success(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

}