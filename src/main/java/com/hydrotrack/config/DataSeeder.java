package com.hydrotrack.config;

import com.hydrotrack.model.User;
import com.hydrotrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> adminOpt = userRepository.findByUsername("admin");
        if (adminOpt.isPresent()) {
            User admin = adminOpt.get();
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            System.out.println("====== ADMIN PASSWORD RESET TO 'admin123' ======");
        }
        
        Optional<User> techOpt = userRepository.findByUsername("tech.juan");
        if (techOpt.isPresent()) {
            User tech = techOpt.get();
            tech.setPasswordHash(passwordEncoder.encode("password123"));
            userRepository.save(tech);
        }
        
        Optional<User> inspOpt = userRepository.findByUsername("insp.maria");
        if (inspOpt.isPresent()) {
            User insp = inspOpt.get();
            insp.setPasswordHash(passwordEncoder.encode("password123"));
            userRepository.save(insp);
        }
        
        Optional<User> viewOpt = userRepository.findByUsername("viewer.jose");
        if (viewOpt.isPresent()) {
            User view = viewOpt.get();
            view.setPasswordHash(passwordEncoder.encode("password123"));
            userRepository.save(view);
        }
    }
}
