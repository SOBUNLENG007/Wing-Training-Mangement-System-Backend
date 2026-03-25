package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.AttendanceRequest;
import com.wtmsbackend.dto.response.AttendanceResponse;

import java.util.List;

public interface AttendanceService {
    List<AttendanceResponse> getAllAttendance(int page, int size);

    List<AttendanceResponse> getAttendanceBySession(Integer sessionId, int page, int size);

    List<AttendanceResponse> getAttendanceByUser(Integer userId, int page, int size);

    AttendanceResponse getAttendanceById(Integer id);

    AttendanceResponse createAttendance(AttendanceRequest request);

    AttendanceResponse updateAttendance(Integer id, AttendanceRequest request);

    void deleteAttendance(Integer id);
}