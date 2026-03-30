package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.MaterialRequest;
import com.wtmsbackend.dto.response.MaterialResponse;

import java.util.List;

public interface MaterialService {
    List<MaterialResponse> getAllMaterials(int page, int size);
    List<MaterialResponse> getAllMaterials();
    List<MaterialResponse> getMaterialsBySession(Integer sessionId, int page, int size);
    List<MaterialResponse> getMaterialsBySession(Integer sessionId);
    MaterialResponse getMaterialById(Integer id);

    MaterialResponse createMaterial(MaterialRequest request);

    MaterialResponse updateMaterial(Integer id, MaterialRequest request);

    void deleteMaterial(Integer id);
}