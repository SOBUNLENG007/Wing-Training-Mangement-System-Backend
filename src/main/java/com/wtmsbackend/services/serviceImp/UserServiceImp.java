package com.wtmsbackend.services.serviceImp;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.request.UserUpdateRequest;
import com.wtmsbackend.dto.response.UserResponse;
import com.wtmsbackend.models.Department;
import com.wtmsbackend.models.User;
import com.wtmsbackend.models.Until.Role;
import com.wtmsbackend.repositories.DepartmentRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final Logger logger;

    @Override
    public List<UserResponse> getAllUsers(int page, int size) {

        // Logging the request parameters
        logger.info("Request data {}", String.format("Page: %d, Size: %d", page, size));

        // Mapper to convert Entity to DTO
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findAll(pageRequest);
        List<UserResponse> users = usersPage.getContent().stream().map(user -> modelMapper.map(user, UserResponse.class)).toList();
        // Map the Page of Entities to a Page of DTOs
        return users;
    }


    @Override
    public UserResponse getUserById(Integer id) {

        logger.info("Request data {}", String.format("User ID: %d", id));

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {

        logger.info("Request data {}", String.format("User Email: %s", email));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapToUserResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest request) {

        logger.info("Request data {}", Object.class.cast(request));

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found with ID: " + request.getDepartmentId()));
        }

        User user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail()).department(department).password(passwordEncoder.encode(request.getPassword())).phoneNumber(request.getPhoneNumber()).address(request.getAddress()).role(Role.EMPLOYEE).status(true).build();

        return mapToUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Integer id, UserUpdateRequest request) {

        logger.info("Request data {}", String.format("User ID: %d, Update Data: %s", id, Object.class.cast(request)));

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Update fields if they are provided
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getStatus() != null) user.setStatus(request.getStatus());

        return mapToUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Integer id) {

        logger.info("Request data {}", String.format("User ID: %d", id));

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Soft Delete: Change status to false instead of deleting from DB
        user.setStatus(false);
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getUsersByDepartment(Integer departmentId, int page, int size) {

        logger.info("Request data {}", String.format("Department ID: %d, Page: %d, Size: %d", departmentId, page, size));
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> usersPage = userRepository.findByDepartmentId(departmentId, pageRequest);

        for (User user : usersPage) {
            modelMapper.map(user, UserResponse.class);
        }
        List<UserResponse> users = usersPage.getContent().stream().map(user -> modelMapper.map(user, UserResponse.class)).toList();

        return users;
    }

    @Override
    public void userResetPassword(String newPassword, String oldPassword, String newPassword1) {

    }

    private UserResponse mapToUserResponse(User user) {

        logger.info("Response  :{}", Object.class.cast(user));

        return UserResponse.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail()).phoneNumber(user.getPhoneNumber()).address(user.getAddress()).status(user.getStatus()).departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null).departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null).role(user.getRole() != null ? user.getRole().name() : null).build();
    }

    @Override
    public UserResponse updateRole(Integer id, Role role) {

        logger.info("Request data {}", String.format("User ID: %d, Role: %s", id, role));

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        user.setRole(role);

        return mapToUserResponse(userRepository.save(user));
    }
}