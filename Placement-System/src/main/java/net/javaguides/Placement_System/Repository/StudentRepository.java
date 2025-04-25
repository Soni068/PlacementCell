package net.javaguides.Placement_System.Repository;

import jakarta.transaction.Transactional;
import net.javaguides.Placement_System.entity.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentModel, String> {
    StudentModel findByEmail(String email);



    @Modifying
    @Transactional
    @Query("UPDATE StudentModel s SET s.marks10 = :marks10, s.marks12 = :marks12, s.marksUg = :marksUg, s.marksPg = :marksPg WHERE s.stud_id = :stud_id")
    int updateMarks(@Param("marks10") Double marks10,
                    @Param("marks12") Double marks12,
                    @Param("marksUg") Double marksUg,
                    @Param("marksPg") Double marksPg,
                    @Param("stud_id") String stud_id);

    @Modifying
    @Transactional
    @Query("UPDATE StudentModel s SET s.resumePath = :resumePath WHERE s.stud_id = :stud_id")
    int uploadResume(@Param("resumePath")String resumePath,
                     @Param("stud_id") String stud_id);

    @Modifying
    @Transactional
    @Query("UPDATE StudentModel s SET s.certificatePaths = :certificatePaths WHERE s.stud_id = :stud_id")
    int uploadCertificates(@Param("certificatePaths") List<String> certificatePaths, @Param("stud_id") String stud_id);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "UPDATE StudentModel s SET s.password=:encrypted where s.email=:email")
    void updateStudentPass(String email, String encrypted);

   //List<StudentModel> findByStatus(Status status1);
   List<StudentModel> findByStatus1(StudentModel.Status status);
}
