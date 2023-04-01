package me.vudb.backend.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import me.vudb.backend.rso.Rso;
import me.vudb.backend.university.University;
import me.vudb.backend.user.models.Admin;
import me.vudb.backend.user.models.Student;
import me.vudb.backend.user.models.SuperAdmin;
import me.vudb.backend.user.models.User;
import me.vudb.backend.user.repository.AdminRepository;
import me.vudb.backend.user.repository.StudentRepository;
import me.vudb.backend.user.repository.SuperAdminRepository;
import me.vudb.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SuperAdminRepository superAdminRepository;

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       SuperAdminRepository superAdminRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public User saveExisting(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public List<SuperAdmin> findAllAdmin() {
        return superAdminRepository.findAll();
    }

    public SuperAdmin createSuperAdmin(User user) {
        User savedUser = save(user);
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setUser(savedUser);
        superAdmin.setVerification(UUID.randomUUID().toString());
        return superAdminRepository.save(superAdmin);
    }

    public SuperAdmin findSuperAdminByUserId(String email) {
        return superAdminRepository.findByUserEmail(email);
    }

    public SuperAdmin setVerificationCode(String superAdminId, String newVerificationCode) {
        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
                .orElseThrow(() -> new EntityNotFoundException("SuperAdmin not found"));
        superAdmin.setVerification(newVerificationCode);
        return superAdminRepository.save(superAdmin);
    }


    public Set<Rso> findUserRso(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getRso();
    }

    @Transactional
    public Student createStudent(User user, University university) {
        User savedUser = save(user);
        Student student = new Student();
        student.setUser(savedUser);
        student.setUniversity(university);

        try {
            studentRepository.save(student);
        } catch (Exception e) {
            userRepository.delete(savedUser);
            throw e;
        }

        return student;
    }


    public University findStudentUniversityByEmail(String username) {
        return studentRepository.findByUserEmail(username).getUniversity();
    }

    public Student findStudentByEmail(String username) {
        return studentRepository.findByUserEmail(username);
    }

    public SuperAdmin findSuperAdminByEmail(String username) {
        return superAdminRepository.findByUserEmail(username);
    }

    public Admin findAdminByUserEmail(String username) {
        return adminRepository.findByUserEmail(username);
    }

    public Optional<User> findByEmailOpt(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}
