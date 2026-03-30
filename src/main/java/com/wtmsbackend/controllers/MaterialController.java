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
import com.wtmsbackend.dto.request.MaterialRequest;
import com.wtmsbackend.dto.response.MaterialResponse;
import com.wtmsbackend.services.MaterialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Materials", description = "Endpoints for managing training materials")
public class MaterialController {

    private final MaterialService materialService;

    @Operation(summary = "Get all materials", description = "Retrieves a list of all training materials in the system.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getAllMaterials() {
        List<MaterialResponse> materials = materialService.getAllMaterials();
        ApiResponse<List<MaterialResponse>> response = ApiResponse.<List<MaterialResponse>>builder()
                .message("Materials fetched successfully!")
                .success(true)
                .payload(materials)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get materials by Session ID", description = "Retrieves a list of materials assigned to a specific training session.")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getMaterialsBySession(@PathVariable Integer sessionId) {
        List<MaterialResponse> materials = materialService.getMaterialsBySession(sessionId);
        ApiResponse<List<MaterialResponse>> response = ApiResponse.<List<MaterialResponse>>builder()
                .message("Session materials fetched successfully!")
                .success(true)
                .payload(materials)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get material by ID", description = "Retrieves specific material details using its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaterialResponse>> getMaterialById(@PathVariable Integer id) {
        MaterialResponse material = materialService.getMaterialById(id);

        ApiResponse<MaterialResponse> response = ApiResponse.<MaterialResponse>builder().message("Material details fetched successfully!").success(true).payload(material).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new material", description = "Uploads/Creates a new training material. Requires ADMIN or TRAINER role.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<MaterialResponse>> createMaterial(@Valid @RequestBody MaterialRequest request) {
        MaterialResponse material = materialService.createMaterial(request);

        ApiResponse<MaterialResponse> response = ApiResponse.<MaterialResponse>builder().message("Material created successfully!").success(true).payload(material).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a material", description = "Updates details of an existing material. Requires ADMIN or TRAINER role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<MaterialResponse>> updateMaterial(@PathVariable Integer id, @Valid @RequestBody MaterialRequest request) {
        MaterialResponse material = materialService.updateMaterial(id, request);

        ApiResponse<MaterialResponse> response = ApiResponse.<MaterialResponse>builder().message("Material updated successfully!").success(true).payload(material).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a material", description = "Permanently deletes a material from the database. Requires ADMIN or TRAINER role.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<Void>> deleteMaterial(@PathVariable Integer id) {
        materialService.deleteMaterial(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder().message("Material deleted successfully!").success(true).payload(null).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.ok(response);
    }
}