package net.javaguides.Placement_System.controller;


import jakarta.persistence.Column;
import net.javaguides.Placement_System.Repository.AdminRepository;
import net.javaguides.Placement_System.entity.AdminModel;
import net.javaguides.Placement_System.entity.CompanyModel;
import net.javaguides.Placement_System.entity.StudentModel;
import net.javaguides.Placement_System.service.AdminService;
import net.javaguides.Placement_System.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/admin/login")
    public String login(@RequestBody  AdminLoginRequest loginRequest)
    {
        AdminModel admin = adminRepository.findByEmail(loginRequest.getEmail());

        // Check if admin exists and validate the password
        if (admin != null && admin.getPassword().equals(loginRequest.getPassword())) {

            return "Admin login successful!";
        } else {
            return "Invalid username or password!";
        }
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<AdminModel> updateAdminPassword(@RequestParam String email, @RequestParam String oldPassword, @RequestParam String newPassword) {
        AdminModel AdminModel = adminService.updateAdminPassword(email, oldPassword, newPassword);
        return ResponseEntity.ok(AdminModel);
    }




    @PutMapping("/update")
    public String updateAdminDetails(@RequestParam String contEmail,
                                     @RequestParam Long phone,
                                     @RequestParam String address) {
        // Call the service method to update the details
        int updatedRecords = adminService.updateAdminDetails(contEmail, phone, address);

        if (updatedRecords > 0) {
            return "Admin details updated successfully!";
        } else {
            return "Failed to update admin details.";
        }
    }

    @PutMapping("/updateEmail")
    public ResponseEntity<String> updateEmail(@RequestParam String email) {
        int updated = adminService.updateAdminEmail(email);
        if (updated > 0) {
            return ResponseEntity.ok("Admin email updated successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update admin email.");
        }
    }


    @GetMapping("/ContactList")
    public List<AdminModel> getAllStudents(){
        return adminService.getContact();
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required!");
        }

        AdminModel admin = adminService.findByEmail(email);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found in database!");
        }

        String token = adminService.generatePasswordResetToken(email);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating reset token.");
        }

        // Send reset link via email
        String subject = "Reset Your Admin Account Password";
        String message = "<p>Dear Admin,</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href='http://localhost:3000/reset-password/admin?token=" + token + "'>Reset Password</a>"
                + "<p>This link is valid for 15 minutes.</p>"
                + "<p>Regards,</p><p><strong>Placement System Team</strong></p>";

        try {
            emailService.sendEmail(email, subject, message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }

        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    // ✅ Reset Password using token
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (token == null || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Token and new password are required!");
        }

        boolean success = adminService.resetPassword(token, newPassword);


        if (success) {
            return ResponseEntity.ok("Password reset successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token!");
        }
    }
    }


class AdminLoginRequest
{
    private Long id;
    private String email;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}