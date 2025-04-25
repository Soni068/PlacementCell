package net.javaguides.Placement_System.Repository;

import net.javaguides.Placement_System.entity.CompanyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import net.javaguides.Placement_System.entity.CompanyModel.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyModel, String> {

    List<CompanyModel> findByStatus(Status status);

    Optional <CompanyModel> findByEmail(String email);

    Optional<CompanyModel> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CompanyModel cm SET cm.password=:Pass where cm.email=:email")
    int updateCompanyPass(String email, String Pass);
}
