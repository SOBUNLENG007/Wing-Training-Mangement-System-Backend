package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.GradeRequest;
import com.wtmsbackend.dto.request.SubmissionRequest;
import com.wtmsbackend.dto.response.SubmissionResponse;

import org.springframework.data.domain.Page;

import java.util.List;

public interface SubmissionService {
    List<SubmissionResponse> getAllSubmissions();

    List<SubmissionResponse> getSubmissionsByAssignment(Integer assignmentId);

    List<SubmissionResponse> getSubmissionsByEmployee(Integer employeeId);

    SubmissionResponse getSubmissionById(Integer id);

    // Employee actions
    SubmissionResponse createSubmission(SubmissionRequest request);

    SubmissionResponse updateSubmissionFile(Integer id, SubmissionRequest request);

    // Trainer actions
    SubmissionResponse gradeSubmission(Integer id, GradeRequest request);

    void deleteSubmission(Integer id);
}