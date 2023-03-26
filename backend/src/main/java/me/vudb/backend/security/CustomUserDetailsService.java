package me.vudb.backend.security;

import me.vudb.backend.user.models.User;
import me.vudb.backend.user.repository.AdminRepository;
import me.vudb.backend.user.repository.StudentRepository;
import me.vudb.backend.user.repository.SuperAdminRepository;
import me.vudb.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SuperAdminRepository superAdminRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Check if the user is a SUPER_ADMIN
        if (superAdminRepository.findById(user.getId()).isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        }

        if (adminRepository.findById(user.getId()).isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if (studentRepository.findById(user.getId()).isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        }

        return new CustomUserDetails(user, authorities);
    }
}
