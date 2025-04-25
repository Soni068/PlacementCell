package net.javaguides.Placement_System.service;


import net.javaguides.Placement_System.Repository.CompanyRepository;
import net.javaguides.Placement_System.entity.CompanyModel;
import net.javaguides.Placement_System.entity.CompanyModel.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    private final ConcurrentHashMap<String, TokenEntry> tokenStorage = new ConcurrentHashMap<>();

    public CompanyModel updateCompanyPassword(String email, String oldPassword, String newPassword) {
        CompanyModel companyDetails = companyRepository.findByEmail(email).get();
        if(oldPassword.equals(companyDetails.getPassword())){
            companyRepository.updateCompanyPass(email, newPassword);
            return companyRepository.findByEmail(email).get();
        }
        return null;
    }

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


    @Autowired
    private JavaMailSender emailSender;




    public CompanyModel registerCompany(CompanyModel companyModel)
    {
        companyModel.setStatus(CompanyModel.Status.PENDING);
        return companyRepository.save(companyModel);
    }

    public List<CompanyModel> getCompaniesWithPending()
    {
        return companyRepository.findByStatus(Status.PENDING);
    }

    public List<CompanyModel> getCompaniesWithAccept()
    {
        return companyRepository.findByStatus(Status.ACCEPTED);
    }

    public CompanyModel approveCompany(String name)
    {
        CompanyModel companyModel = companyRepository.findById(name).orElseThrow(() -> new RuntimeException("Company not found"));
        companyModel.setStatus(CompanyModel.Status.ACCEPTED);
        sendNotificationEmail(companyModel, "Your registration has been approved");
        return companyRepository.save(companyModel);
    }

    public void rejectCompany(String name)
    {
        CompanyModel companyModel = companyRepository.findById(name).orElseThrow(() -> new RuntimeException("Company not found"));
        companyModel.setStatus(CompanyModel.Status.REJECTED);
        companyRepository.delete(companyModel);
        sendNotificationEmail(companyModel, "Your registration has been rejected");

    }

    public String generatePasswordResetToken(String email) {
        CompanyModel company = findByEmail(email);
        if (company == null) {
            System.out.println("⚠️ Company with email " + email + " not found!");
            return null;
        }

        String token = UUID.randomUUID().toString();
        tokenStorage.put(email, new TokenEntry(token, LocalDateTime.now().plusMinutes(15)));

        System.out.println("✅ Token generated for " + email + ": " + token);
        return token;
    }

    public CompanyModel findByEmail(String email) {
        return companyRepository.findByEmail(email).orElse(null);
    }


    public CompanyModel getCompanyByName(String name) {
        return companyRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Company not found with name: " + name));
    }


    public boolean resetPassword(String token, String newPassword) {
        for (Map.Entry<String, TokenEntry> entry : tokenStorage.entrySet()) {
            if (entry.getValue().getToken().equals(token) && entry.getValue().getExpiryTime().isAfter(LocalDateTime.now())) {
                CompanyModel company = findByEmail(entry.getKey());
                if (company != null) {
                    company.setPassword(newPassword); // optionally encode the password
                    companyRepository.save(company);
                    tokenStorage.remove(entry.getKey());
                    return true;
                }
            }
        }
        return false;
    }



    private void sendNotificationEmail(CompanyModel companyModel, String message)
    {
        try{
            MimeMessageHelper helper = new MimeMessageHelper(emailSender.createMimeMessage());
            helper.setTo(companyModel.getEmail());
            helper.setSubject("Company Registration Status");
            helper.setText(message);
            emailSender.send(helper.getMimeMessage());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public CompanyModel updateCompanyByName(String name, CompanyModel updatedCompany) {
        CompanyModel existingCompany = companyRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Company not found with name: " + name));

        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setDescription(updatedCompany.getDescription());
        // Optional: Update password only if it's not empty or null
        if (updatedCompany.getPassword() != null && !updatedCompany.getPassword().isEmpty()) {
            existingCompany.setPassword(updatedCompany.getPassword());
        }

        return companyRepository.save(existingCompany);
    }


    public CompanyModel loginCompany(String email, String password)
    {
        Optional <CompanyModel> company = companyRepository.findByEmail(email);

        if (company.isPresent()) {
             CompanyModel companyModel = company.get();

            if (companyModel.getStatus() == CompanyModel.Status.ACCEPTED) {
                if (companyModel.getPassword().equals(password)) {
                    return companyModel;
                } else {
                    return null;
                }

            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }
}
