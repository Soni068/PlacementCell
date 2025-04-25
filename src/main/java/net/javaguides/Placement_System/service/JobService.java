package net.javaguides.Placement_System.service;

import net.javaguides.Placement_System.Repository.StudentRepository;
import net.javaguides.Placement_System.entity.CompanyModel;
import net.javaguides.Placement_System.entity.PostJobs;
import net.javaguides.Placement_System.Repository.CompanyRepository;
import net.javaguides.Placement_System.Repository.PostJobsRepository;
import net.javaguides.Placement_System.entity.StudentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private PostJobsRepository postJobsRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private StudentRepository studentRepository;

    public PostJobs postJob(String companyName, PostJobs job) {
        CompanyModel company = companyRepository.findById(companyName).orElseThrow(() -> new RuntimeException("Company not found"));
        job.setCompany(company);  // Set the company to the job
        return postJobsRepository.save(job);  // Save the job
    }

    public List<PostJobs> getJobsByCompany(String companyName) {
        return postJobsRepository.findByCompanyName(companyName);
    }

    /*public List<PostJobs> getJobsById(Long id){
        return postJobsRepository.findByJobId(id);
    }*/

    public Optional<PostJobs> getJobById(Long jobId) {
        return postJobsRepository.findById(jobId);
    }


    public List<PostJobs> getRelevantJobsForStudent(String studId) {
        // Fetch student details based on studId
        StudentModel student = studentRepository.findById(studId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get student's CGPA and courses
        double ugCgpa = student.getMarksUg(); // Assuming studentModel has marksUg
        double pgCgpa = student.getMarksPg(); // Assuming studentModel has marksPg
        String studentCourse = student.getCourse(); // Assuming student has a single course (you can change this to List if needed)

        // Fetch all jobs
        List<PostJobs> allJobs = postJobsRepository.findAll();

        // Filter jobs based on CGPA and course
        List<PostJobs> relevantJobs = new ArrayList<>();
        for (PostJobs job : allJobs) {
            // Check if student meets CGPA criteria for both UG and PG
            boolean meetsCgpaCriteria = (job.getUgCgpa() <= ugCgpa) && (job.getPgCgpa() <= pgCgpa);

            // Check if the student has the required course
            boolean hasRequiredCourse = job.getCourses().contains(studentCourse);

            if (meetsCgpaCriteria && hasRequiredCourse) {
                relevantJobs.add(job);
            }
        }

        return relevantJobs;
    }

    public Optional<PostJobs> getJobDetails(Long jobId) {
        // Fetch job details based on the jobId from the database
        return postJobsRepository.findById(jobId);
    }

}