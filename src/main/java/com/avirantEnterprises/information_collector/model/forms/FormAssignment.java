package com.avirantEnterprises.information_collector.model.forms;

import com.avirantEnterprises.information_collector.model.login.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="form_assignment")
public class FormAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formName;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;
    private LocalDateTime assignedAt;


}
