package com.wtmsbackend.services.serviceImp;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.wtmsbackend.dto.request.MaterialRequest;
import com.wtmsbackend.dto.response.MaterialResponse;
import com.wtmsbackend.models.Material;
import com.wtmsbackend.models.Session;
import com.wtmsbackend.models.User;
import com.wtmsbackend.repositories.MaterialRepository;
import com.wtmsbackend.repositories.SessionRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.services.MaterialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialServiceImp implements MaterialService {

    private final MaterialRepository materialRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public List<MaterialResponse> getAllMaterials(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<MaterialResponse> material = materialRepository.findAll(pageRequest).map(this::mapToResponse).getContent();

        return material;
    }

    @Override
    public List<MaterialResponse> getMaterialsBySession(Integer sessionId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<MaterialResponse> material = materialRepository.findBySessionId(sessionId, pageRequest).map(this::mapToResponse).getContent();
        return material;
    }

    @Override
    public MaterialResponse getMaterialById(Integer id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));
        return mapToResponse(material);
    }

    @Override
    public MaterialResponse createMaterial(MaterialRequest request) {
        Session session = sessionRepository.findById(request.getSessionId()).orElseThrow(() -> new RuntimeException("Session not found with ID: " + request.getSessionId()));

        User trainer = userRepository.findById(request.getTrainerId()).orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + request.getTrainerId()));

        Material material = Material.builder().title(request.getTitle()).fileUrl(request.getFileUrl()).session(session).trainer(trainer).build();

        return mapToResponse(materialRepository.save(material));
    }

    @Override
    public MaterialResponse updateMaterial(Integer id, MaterialRequest request) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));

        material.setTitle(request.getTitle());
        material.setFileUrl(request.getFileUrl());

        // Update relationships if they changed
        if (!material.getSession().getId().equals(request.getSessionId())) {
            Session session = sessionRepository.findById(request.getSessionId()).orElseThrow(() -> new RuntimeException("Session not found"));
            material.setSession(session);
        }

        if (!material.getTrainer().getId().equals(request.getTrainerId())) {
            User trainer = userRepository.findById(request.getTrainerId()).orElseThrow(() -> new RuntimeException("Trainer not found"));
            material.setTrainer(trainer);
        }

        return mapToResponse(materialRepository.save(material));
    }

    @Override
    public void deleteMaterial(Integer id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));
        materialRepository.delete(material);
    }

    @Override
    public List<MaterialResponse> getAllMaterials() {
        return materialRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<MaterialResponse> getMaterialsBySession(Integer sessionId) {
        return materialRepository.findAll().stream()
            .filter(m -> m.getSession() != null && m.getSession().getId().equals(sessionId))
            .map(this::mapToResponse)
            .toList();
    }

    private MaterialResponse mapToResponse(Material material) {
        return MaterialResponse.builder().id(material.getId()).title(material.getTitle()).fileUrl(material.getFileUrl()).sessionId(material.getSession() != null ? material.getSession().getId() : null).sessionTitle(material.getSession() != null ? material.getSession().getTitle() : null).trainerId(material.getTrainer() != null ? material.getTrainer().getId() : null).trainerName(material.getTrainer() != null ? (material.getTrainer().getFirstName() + " " + material.getTrainer().getLastName()) : null).createdAt(material.getCreatedAt()).build();
    }
}