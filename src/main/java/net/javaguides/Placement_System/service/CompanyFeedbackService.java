package net.javaguides.Placement_System.service;

import net.javaguides.Placement_System.Repository.CompanyFeedbackRepository;
import net.javaguides.Placement_System.entity.CompanyFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyFeedbackService {
    @Autowired
    private CompanyFeedbackRepository companyFeedbackRepository;
    public CompanyFeedback saveFeedback(CompanyFeedback feedback) {
        return companyFeedbackRepository.save(feedback);
    }

    public List<CompanyFeedback> getAllFeedback() {
        return companyFeedbackRepository.findAll();
    }

    public List<CompanyFeedback> getFeedbackByCompanyName(String companyName) {
        return companyFeedbackRepository.findByCompany_Name(companyName);
    }
}
