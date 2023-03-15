package me.vudb.backend.service;

import me.vudb.backend.models.CustomUserDetails;
import me.vudb.backend.models.Student;
import me.vudb.backend.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final SuperAdminService superAdminService;
    private final StudentService studentService;

    public CustomUserDetailsService(UserService userService, SuperAdminService superAdminService, StudentService studentService) {
        this.userService = userService;
        this.superAdminService = superAdminService;
        this.studentService = studentService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        String role;
        if (superAdminService.existsById(user.getId())) {
            role = "SUPER_ADMIN";
        } else if (studentService.existsById(user.getId())) {
            role = "STUDENT";
        } else {
            throw new IllegalStateException("User role not found for email: " + email);
        }

        return new CustomUserDetails(user, role);
    }
}
