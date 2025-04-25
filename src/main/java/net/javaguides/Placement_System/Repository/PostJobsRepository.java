package net.javaguides.Placement_System.Repository;
import net.javaguides.Placement_System.entity.PostJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostJobsRepository extends JpaRepository<PostJobs, Long>{
    public List<PostJobs> findByCompanyName(String companyName);
     //Optional<PostJobs> findByJobId(Long id);
}
