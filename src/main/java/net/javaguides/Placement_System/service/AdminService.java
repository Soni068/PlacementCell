package net.javaguides.Placement_System.service;

import net.javaguides.Placement_System.Repository.AdminRepository;
import net.javaguides.Placement_System.entity.CompanyModel;
import net.javaguides.Placement_System.entity.StudentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.javaguides.Placement_System.entity.AdminModel;
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JavaMailSender emailSender;

    // In-memory token storage (email -> TokenEntry)
    private final ConcurrentHashMap<String, TokenEntry> tokenStorage = new ConcurrentHashMap<>();

    public AdminModel updateAdminPassword(String email, String oldPassword, String newPassword) {
        AdminModel adminModel = adminRepository.findByEmail(email);
        if(oldPassword.equals(adminModel.getPassword())){
            adminRepository.updateAdminPass(email, newPassword);
            return adminRepository.findByEmail(email);
        }
        return null;
    }

    // ✅ Token entry inner class
    static class TokenEntry {
        private final String token;
        private final LocalDateTime expiryTime;

        public TokenEntry(String token, LocalDateTime expiryTime) {
            this.token = token;
            this.expiryTime = expiryTime;
        }

        public String getToken() {
            return token;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }

    public int updateAdminDetails(String contEmail, Long phone, String address) {
        // Calling the repository method to update the admin's details
        return adminRepository.updateContact(contEmail, phone, address);
    }

    public int updateAdminEmail(String email) {
        return adminRepository.updateAdminEmail(email);
    }

    public List<AdminModel> getContact() {
        return adminRepository.findAll();
    }

    // ✅ Find admin by email
    public AdminModel findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    // ✅ Generate password reset token
    public String generatePasswordResetToken(String email) {
        AdminModel admin = findByEmail(email);
        if (admin == null) {
            System.out.println("⚠️ Admin with email " + email + " not found!");
            return null;
        }

        String token = UUID.randomUUID().toString();
        tokenStorage.put(email, new TokenEntry(token, LocalDateTime.now().plusMinutes(15)));

        System.out.println("✅ Token generated for " + email + ": " + token);
        return token;
    }

    // ✅ Reset password using token
    public boolean resetPassword(String token, String newPassword) {
        for (Map.Entry<String, TokenEntry> entry : tokenStorage.entrySet()) {
            TokenEntry tokenEntry = entry.getValue();

            if (tokenEntry.getToken().equals(token) &&
                    tokenEntry.getExpiryTime().isAfter(LocalDateTime.now())) {

                AdminModel admin = findByEmail(entry.getKey());
                if (admin != null) {
                    admin.setPassword(newPassword); // Optionally encode password here
                    adminRepository.save(admin);

                    tokenStorage.remove(entry.getKey());
                    return true;
                }
            }
        }
        return false;
    }
}
