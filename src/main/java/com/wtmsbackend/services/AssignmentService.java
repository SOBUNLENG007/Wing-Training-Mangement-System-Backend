package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.AssignmentRequest;
import com.wtmsbackend.dto.response.AssignmentResponse;

import java.util.List;

public interface AssignmentService {
    List<AssignmentResponse> getAllAssignments(int page, int size);

    List<AssignmentResponse> getAssignmentsBySession(Integer sessionId, int page, int size);

    AssignmentResponse getAssignmentById(Integer id);

    AssignmentResponse createAssignment(AssignmentRequest request);

    AssignmentResponse updateAssignment(Integer id, AssignmentRequest request);

    void deleteAssignment(Integer id);
}