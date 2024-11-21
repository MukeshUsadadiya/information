package com.avirantEnterprises.information_collector.service.login;

import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.repository.login.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        return userLoginRepository.findAll(); // Fetch all user records
    }
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        userLoginRepository.save(user);
    }

    public User findByEmail(String email) {
        Optional<User> user = userLoginRepository.findByEmail(email);
        return user.orElse(null);
    }

    public String generateOtp() {
        return String.valueOf(new Random().nextInt(899999) + 100000); // 6-digit random number
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
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes
            userLoginRepository.save(user);
        }
    }
}
