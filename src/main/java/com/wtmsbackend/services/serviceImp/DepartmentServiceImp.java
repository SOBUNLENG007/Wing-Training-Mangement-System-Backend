package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.DepartmentRequest;
import com.wtmsbackend.dto.response.DepartmentResponse;
import com.wtmsbackend.models.Department;
import com.wtmsbackend.models.User;
import com.wtmsbackend.repositories.DepartmentRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImp implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department name already exists!");
        }

        Department department = Department.builder().name(request.getName()).description(request.getDescription()).status(true).build();

        return mapToResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse updateDepartment(Integer id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

        // Check if they are trying to change the name to one that already exists
        if (!department.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department name already exists!");
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        return mapToResponse(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

        // Soft delete
        department.setStatus(false);
        departmentRepository.save(department);
    }

    @Override
    public DepartmentResponse getDepartmentByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Department department = user.getDepartment();
        if (department == null) {
            throw new RuntimeException("Department not found for user with email: " + email);
        }
        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse getDepartmentByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Department department = user.getDepartment();
        if (department == null) {
            throw new RuntimeException("Department not found for user with ID: " + userId);
        }
        return mapToResponse(department);
    }

    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder().id(department.getId()).name(department.getName()).description(department.getDescription()).status(department.getStatus()).build();
    }
}