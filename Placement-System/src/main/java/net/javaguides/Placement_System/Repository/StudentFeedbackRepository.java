package net.javaguides.Placement_System.Repository;

import net.javaguides.Placement_System.entity.StudentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeedbackRepository extends JpaRepository<StudentFeedback, Long> {
    //List<StudentFeedback> findByStudent_Stud_id(String studId);

    @Query("SELECT sf FROM StudentFeedback sf WHERE sf.student.stud_id = :studId")
    List<StudentFeedback> findFeedbackByStudentId(@Param("studId") String studId);
}
