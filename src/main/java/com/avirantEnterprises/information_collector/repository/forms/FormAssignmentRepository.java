package com.avirantEnterprises.information_collector.repository.forms;

import com.avirantEnterprises.information_collector.model.forms.FormAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormAssignmentRepository extends JpaRepository<FormAssignment,Long> {
    List<FormAssignment>findByUserId(Long userId);
}
