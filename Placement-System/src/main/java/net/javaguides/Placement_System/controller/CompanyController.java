package net.javaguides.Placement_System.controller;

import jakarta.validation.Valid;
import net.javaguides.Placement_System.entity.CompanyModel;
import net.javaguides.Placement_System.service.CompanyService;
import net.javaguides.Placement_System.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:3000")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/companyRegister")
    public CompanyModel register(@Valid @RequestBody CompanyModel companyModel)
    {
        return companyService.registerCompany(companyModel);
    }

    @PutMapping("/approve/{name}")
    public CompanyModel approve(@PathVariable String name)
    {
        return companyService.approveCompany(name);
    }

    @DeleteMapping("/reject/{name}")
    public void reject(@PathVariable String name)
    {
         companyService.rejectCompany(name);
    }

    @GetMapping("/pending")
    public List<CompanyModel> getCompaniesWithPending()
    {
        return companyService.getCompaniesWithPending();
    }

    @GetMapping("/accepted")
    public List<CompanyModel> getCompaniesWithAccept()
    {
        return companyService.getCompaniesWithAccept();
    }

    @PostMapping("/companyLogin")
    public ResponseEntity<Map<String, String>> loginCompany(@RequestBody CompanyModel companyModel)
    {
        CompanyModel company =  companyService.loginCompany(companyModel.getEmail(), companyModel.getPassword());

        if (company != null) {
            // Login is successful, return company name along with success message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Company login successful!");
            response.put("name", company.getName()); // Fetch and return company name
            response.put("email", company.getEmail());
            return ResponseEntity.ok(response);
        } else {
            // Return failure message if login fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid email or password!"));
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<CompanyModel> getCompanyByName(@PathVariable String name) {
        CompanyModel company = companyService.getCompanyByName(name);
        return ResponseEntity.ok(company);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required!");
        }

        CompanyModel company = companyService.findByEmail(email);
        if (company == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found in database!");
        }

        String token = companyService.generatePasswordResetToken(email);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating reset token.");
        }

        // Send reset link via email
        String subject = "Reset Your Company Account Password";
        String message = "<p>Dear Company,</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href='http://localhost:3000/reset-password/company?token=" + token + "'>Reset Password</a>"
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

        boolean success = companyService.resetPassword(token, newPassword);

        if (success) {
            return ResponseEntity.ok("Password reset successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token!");
        }
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<CompanyModel> updateCompany(@PathVariable String name, @RequestBody CompanyModel companyModel) {
        CompanyModel updated = companyService.updateCompanyByName(name, companyModel);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<CompanyModel> updateCompanyPassword(@RequestParam String email, @RequestParam String oldPassword,@RequestParam String newPassword) {
        CompanyModel updated = companyService.updateCompanyPassword(email, oldPassword, newPassword);
        return ResponseEntity.ok(updated);
    }

}
