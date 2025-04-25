package net.javaguides.Placement_System.Repository;

import jakarta.transaction.Transactional;
import net.javaguides.Placement_System.entity.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<AdminModel, Long> {


    @Query("select a from AdminModel a WHERE a.email=:email")
    AdminModel findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE AdminModel a SET a.contEmail = :contEmail, a.phone = :phone, a.address = :address WHERE a.id = 1")
    int updateContact(@Param("contEmail") String contEmail,
                    @Param("phone") Long phone,
                    @Param("address") String address);

    @Modifying
    @Transactional
    @Query("UPDATE AdminModel a SET a.email = :email WHERE a.id = 1")
    int updateAdminEmail(@Param("email") String email);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "UPDATE AdminModel a SET a.password=:Pass where a.email=:email")
    void updateAdminPass(String email, String Pass);




}
