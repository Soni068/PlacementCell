package net.javaguides.Placement_System.Repository;

import net.javaguides.Placement_System.entity.TeamMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TeamRepository extends JpaRepository<TeamMembers, Long> {
}
