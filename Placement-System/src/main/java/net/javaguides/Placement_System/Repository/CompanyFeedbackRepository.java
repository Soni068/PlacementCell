package net.javaguides.Placement_System.Repository;

import net.javaguides.Placement_System.entity.CompanyFeedback;
import net.javaguides.Placement_System.entity.StudentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyFeedbackRepository extends JpaRepository<CompanyFeedback, Long> {
    List<CompanyFeedback> findByCompany_Name(String companyName);
}
