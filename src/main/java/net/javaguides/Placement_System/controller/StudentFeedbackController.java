package net.javaguides.Placement_System.controller;

import net.javaguides.Placement_System.entity.StudentFeedback;
import net.javaguides.Placement_System.service.StudentFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentFeedbackController {
    @Autowired
    private StudentFeedbackService feedbackService;

    @PostMapping
    public StudentFeedback submitFeedback(@RequestBody StudentFeedback feedback) {
        return feedbackService.saveFeedback(feedback);
    }

    @GetMapping
    public List<StudentFeedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/student/{studId}")
    public List<StudentFeedback> getFeedbackByStudent(@PathVariable String studId) {
        return feedbackService.getFeedbackByStudentId(studId);
    }

    @PutMapping("/updatePassword")
    public List<StudentFeedback> updateStudentPass(@PathVariable String studId) {
        return feedbackService.getFeedbackByStudentId(studId);
    }
}
