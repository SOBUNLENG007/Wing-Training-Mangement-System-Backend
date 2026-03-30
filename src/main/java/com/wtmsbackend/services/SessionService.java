package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.SessionRequest;
import com.wtmsbackend.dto.response.SessionResponse;

import java.util.List;

public interface SessionService {
    List<SessionResponse> getAllSessions();

    SessionResponse getSessionById(Integer id);

    SessionResponse createSession(SessionRequest request);

    SessionResponse updateSession(Integer id, SessionRequest request);

    void deleteSession(Integer id);
}