package net.javaguides.Placement_System.Repository;

import net.javaguides.Placement_System.entity.AppliedCandidates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppliedCandidatesRepository extends JpaRepository<AppliedCandidates, Long> {

    List<AppliedCandidates> findByJobId(Long jobId);


    @Query("SELECT CASE WHEN COUNT(ac) > 0 THEN true ELSE false END " +
            "FROM AppliedCandidates ac " +
            "WHERE ac.student.stud_id = :studentId AND ac.job.id = :jobId")
    boolean existsByStudentIdAndJobId(@org.springframework.data.repository.query.Param("studentId") String studentId,
                                      @org.springframework.data.repository.query.Param("jobId") Long jobId);

}
