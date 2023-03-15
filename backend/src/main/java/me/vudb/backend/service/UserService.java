package me.vudb.backend.service;

import jakarta.persistence.EntityNotFoundException;
import me.vudb.backend.models.SuperAdmin;
import me.vudb.backend.models.User;
import me.vudb.backend.repository.StudentRepository;
import me.vudb.backend.repository.SuperAdminRepository;
import me.vudb.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       SuperAdminRepository superAdminRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.superAdminRepository = superAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            System.out.println("null");
        }
        System.out.println(user.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (superAdminRepository.existsById(user.getId())) {
            System.out.println("Super Admin");
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        } else//(studentRepository.existsById(user.getId()))
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public SuperAdmin setVerificationCode(String superAdminId, String newVerificationCode) {
        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
                .orElseThrow(() -> new EntityNotFoundException("SuperAdmin not found"));
        superAdmin.setVerification(newVerificationCode);
        return superAdminRepository.save(superAdmin);
    }
}
