package com.avirantEnterprises.information_collector.repository.login;

import com.avirantEnterprises.information_collector.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByOtp(String otp);
}
