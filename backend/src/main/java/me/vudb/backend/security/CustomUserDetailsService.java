package me.vudb.backend.security;

import me.vudb.backend.models.user.User;
import me.vudb.backend.repository.AdminRepository;
import me.vudb.backend.repository.StudentRepository;
import me.vudb.backend.repository.SuperAdminRepository;
import me.vudb.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
        System.out.println(username);
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Check if the user is a SUPER_ADMIN
        if (superAdminRepository.findById(user.getId()).isPresent()) {
            System.out.println("SUPER_ADMIN");
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        }

        // Check if the user is a STUDENT
        if (studentRepository.findById(user.getId()).isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        }

        // Check if the user is an ADMIN
        if (adminRepository.findById(user.getId()).isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(user, authorities);
    }
}
