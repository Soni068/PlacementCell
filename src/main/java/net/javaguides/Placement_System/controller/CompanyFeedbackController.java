package net.javaguides.Placement_System.controller;

import net.javaguides.Placement_System.entity.CompanyFeedback;
import net.javaguides.Placement_System.service.CompanyFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companyFeedback")
@CrossOrigin(origins = "http://localhost:3000")
public class CompanyFeedbackController {

    @Autowired
    private CompanyFeedbackService feedbackService;

    @PostMapping
    public CompanyFeedback createFeedback(@RequestBody CompanyFeedback feedback) {
        return feedbackService.saveFeedback(feedback);
    }

    @GetMapping
    public List<CompanyFeedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/company/{companyName}")
    public List<CompanyFeedback> getFeedbackByCompany(@PathVariable String companyName) {
        return feedbackService.getFeedbackByCompanyName(companyName);
    }
}
