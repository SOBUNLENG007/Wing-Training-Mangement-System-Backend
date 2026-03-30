//package com.wtmsbackend.init;
//
//import com.wtmsbackend.models.*;
//import com.wtmsbackend.repositories.*;
//import com.wtmsbackend.models.Until.Role;
//import com.wtmsbackend.models.Until.Gender;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer {
//    private final DepartmentRepository departmentRepository;
//    private final UserRepository userRepository;
//    private final SessionRepository sessionRepository;
//    private final AssignmentRepository assignmentRepository;
//    private final AttendanceRepository attendanceRepository;
//    private final MaterialRepository materialRepository;
//    private final SubmissionRepository submissionRepository;
//    private final NotificationRepository notificationRepository;
//    private final OtpRepository otpRepository;
//
//    @PostConstruct
//    public void init() {
//        // 1. Departments
//        List<Department> departments = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            String name = "Department " + i;
//            Department dept = departmentRepository.existsByName(name)
//                    ? departmentRepository.findAll().stream().filter(d -> d.getName().equals(name)).findFirst().orElse(null)
//                    : departmentRepository.save(Department.builder()
//                        .name(name)
//                        .description("Description for " + name)
//                        .status(true)
//                        .build());
//            departments.add(dept);
//        }
//
//        // 2. Users
//        List<User> users = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            String email = "user" + i + "@test.com";
//            User user = userRepository.existsByEmail(email)
//                    ? userRepository.findByEmail(email).orElse(null)
//                    : userRepository.save(User.builder()
//                        .firstName("User" + i)
//                        .lastName("Test" + i)
//                        .email(email)
//                        .password("password" + i)
//                        .phoneNumber("01234567" + i)
//                        .address("Address " + i)
//                        .status(true)
//                        .role(Role.EMPLOYEE)
//                        .gender(Gender.MALE)
//                        .dateOfBirth(LocalDate.of(1990, 1, i))
//                        .department(departments.get(i - 1))
//                        .build());
//            users.add(user);
//        }
//        // 3. Sessions
//        List<Session> sessions = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            sessions.add(Session.builder()
//                    .title("Session " + i)
//                    .description("Description for Session " + i)
//                    .numSession(i)
//                    .startDate(LocalDateTime.now().plusDays(i))
//                    .endDate(LocalDateTime.now().plusDays(i + 1))
//                    .status(true)
//                    .department(departments.get(i - 1))
//                    .instructor(users.get(0))
//                    .build());
//        }
//        sessionRepository.saveAll(sessions);
//
//        // 4. Assignments
//        List<Assignment> assignments = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            assignments.add(Assignment.builder()
//                    .title("Assignment " + i)
//                    .description("Description for Assignment " + i)
//                    .deadline(LocalDateTime.now().plusDays(i + 2))
//                    .totalScore(100)
//                    .session(sessions.get(i - 1))
//                    .build());
//        }
//        assignmentRepository.saveAll(assignments);
//
//        // 5. Materials
//        List<Material> materials = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            materials.add(Material.builder()
//                    .title("Material " + i)
//                    .fileUrl("http://example.com/material" + i)
//                    .session(sessions.get(i - 1))
//                    .trainer(users.get(0))
//                    .build());
//        }
//        materialRepository.saveAll(materials);
//
//        // 6. Attendance
//        List<Attendance> attendances = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            attendances.add(Attendance.builder()
//                    .user(users.get(i - 1))
//                    .session(sessions.get(i - 1))
//                    .attendanceDate(LocalDate.now().plusDays(i))
//                    .status("PRESENT")
//                    .markedBy(users.get(0))
//                    .build());
//        }
//        attendanceRepository.saveAll(attendances);
//
//        // 7. Submissions
//        List<Submission> submissions = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            submissions.add(Submission.builder()
//                    .employee(users.get(i - 1))
//                    .assignment(assignments.get(i - 1))
//                    .fileUrl("http://example.com/submission" + i)
//                    .score(80 + i)
//                    .feedback("Feedback for submission " + i)
//                    .build());
//        }
//        submissionRepository.saveAll(submissions);
//
//        // 8. Notifications
//        List<Notification> notifications = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            notifications.add(Notification.builder()
//                    .user(users.get(i - 1))
//                    .message("Notification message " + i)
//                    .isRead(false)
//                    .build());
//        }
//        notificationRepository.saveAll(notifications);
//
//        // 9. OTPs
//        List<Otp> otps = new ArrayList<>();
//        for (int i = 1; i <= 5; i++) {
//            otps.add(Otp.builder()
//                    .otpCode("12345" + i)
//                    .isVerified(false)
//                    .user(users.get(i - 1))
//                    .build());
//        }
//        otpRepository.saveAll(otps);
//    }
//}
