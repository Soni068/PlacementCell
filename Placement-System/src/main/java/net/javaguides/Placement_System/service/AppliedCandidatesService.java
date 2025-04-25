package net.javaguides.Placement_System.service;

import net.javaguides.Placement_System.Repository.AppliedCandidatesRepository;
import net.javaguides.Placement_System.Repository.PostJobsRepository;
import net.javaguides.Placement_System.Repository.StudentRepository;
import net.javaguides.Placement_System.entity.AppliedCandidates;
import net.javaguides.Placement_System.entity.PostJobs;
import net.javaguides.Placement_System.entity.StudentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppliedCandidatesService {
    @Autowired
    private AppliedCandidatesRepository appliedRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private PostJobsRepository jobRepo;

    public AppliedCandidates applyToJob(String studentId, Long jobId) {


        StudentModel student = studentRepo.findById(studentId).orElseThrow(() ->
                new RuntimeException("Student not found with ID: " + studentId));

        PostJobs job = jobRepo.findById(jobId).orElseThrow(() ->
                new RuntimeException("Job not found with ID: " + jobId));

        AppliedCandidates application = new AppliedCandidates();
        application.setStudent(student);
        application.setJob(job);
        application.setStudentName(student.getName());
        application.setJobTitle(job.getTitle());
        application.setCompanyName(job.getCompany().getName());

        return appliedRepo.save(application);
    }



    public List<AppliedCandidates> getAllApplications() {
        return appliedRepo.findAll();
    }

    public boolean isCandidateAppliedForJob(String studentId, Long jobId) {
        return appliedRepo.existsByStudentIdAndJobId(studentId, jobId);

    }

    public List<AppliedCandidates> getCandidatesByJobId(Long jobId) {
        return appliedRepo.findByJobId(jobId);
    }

}
