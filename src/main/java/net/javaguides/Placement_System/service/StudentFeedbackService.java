package net.javaguides.Placement_System.service;

import net.javaguides.Placement_System.Repository.StudentFeedbackRepository;
import net.javaguides.Placement_System.Repository.StudentRepository;
import net.javaguides.Placement_System.entity.StudentFeedback;
import net.javaguides.Placement_System.entity.StudentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentFeedbackService {
    @Autowired
    private StudentFeedbackRepository feedbackRepository;

    @Autowired
    private StudentRepository studentRepository;

    public StudentFeedback saveFeedback(StudentFeedback feedback) {
        if (feedback.getStudent() != null && feedback.getStudent().getStud_id() != null) {
            // Fetch the full student record
            StudentModel fullStudent = studentRepository.findById(feedback.getStudent().getStud_id()).orElse(null);
            if (fullStudent != null) {
                feedback.setStudent(fullStudent); // ensure we save a managed entity
                feedback.setStudentName(fullStudent.getName()); // set name from DB
            }
        }
        return feedbackRepository.save(feedback);
    }

    public List<StudentFeedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public List<StudentFeedback> getFeedbackByStudentId(String studId) {
        return feedbackRepository.findFeedbackByStudentId(studId);
    }
}
