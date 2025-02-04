package com.avirantEnterprises.information_collector.service.login;

import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.repository.login.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserLoginService {
    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserLoginService(UserLoginRepository userLoginRepository, PasswordEncoder passwordEncoder) {
        this.userLoginRepository = userLoginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userLoginRepository.findAll();
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userLoginRepository.save(user);
    }

    public User findByEmail(String email) {
        Optional<User> user = userLoginRepository.findByEmail(email);
        return user.orElse(null);
    }

    public String generateOtp() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    public boolean verifyOtp(String email, String otp) {
        User user = findByEmail(email);
        return user != null
                && otp.equals(user.getOtp())
                && user.getOtpExpiry() != null
                && user.getOtpExpiry().isAfter(LocalDateTime.now());
    }

    public void updateOtp(String email, String otp) {
        User user = findByEmail(email);
        if (user != null) {
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            userLoginRepository.save(user);
        }
    }
    public Optional<User> findByUsername(String username) {
        return userLoginRepository.findByUsername(username);  // Fetch user from the database
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword); // This compares the raw password with the encoded one
    }
}
