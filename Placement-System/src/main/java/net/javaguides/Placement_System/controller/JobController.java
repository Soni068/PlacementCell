package net.javaguides.Placement_System.controller;
import net.javaguides.Placement_System.Repository.PostJobsRepository;
import net.javaguides.Placement_System.entity.PostJobs;
import net.javaguides.Placement_System.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private PostJobsRepository postJobsRepository;
    // Endpoint to post a job for a company
    @PostMapping("/{companyName}")
    public ResponseEntity<PostJobs> postJob(@PathVariable String companyName, @RequestBody PostJobs job) {
        PostJobs postedJob = jobService.postJob(companyName, job);
        return new ResponseEntity<>(postedJob, HttpStatus.CREATED);
    }

    @GetMapping("/{companyName}")
    public ResponseEntity<List<PostJobs>> getJobsByCompany(@PathVariable String companyName) {
        List<PostJobs> jobs = jobService.getJobsByCompany(companyName);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<PostJobs> getJobsById(@PathVariable Long id) {
        Optional<PostJobs> jobs = jobService.getJobById(id);
        if (jobs.isPresent()) {
            return new ResponseEntity<>(jobs.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Job not found
        }
    }

    @PutMapping("/job/{id}")
    public ResponseEntity<PostJobs> updateJob(@PathVariable Long id, @RequestBody PostJobs updatedJob) {
        try {
            Optional<PostJobs> existingJob = jobService.getJobById(id);
            if (!existingJob.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Job not found
            }

            PostJobs job = existingJob.get();

            // Update the job fields
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setLocation(updatedJob.getLocation());
            job.setSalary(updatedJob.getSalary());
            job.setCourses(updatedJob.getCourses());
            job.setType(updatedJob.getType());
            job.setDeadline(updatedJob.getDeadline());
            job.setSkills(updatedJob.getSkills());
            job.setUgCgpa(updatedJob.getUgCgpa());
            job.setPgCgpa(updatedJob.getPgCgpa());
            job.setCompanyWebsite(updatedJob.getCompanyWebsite());

            // Save the updated job
            postJobsRepository.save(job);

            return new ResponseEntity<>(job, HttpStatus.OK); // Return the updated job
        }
        catch (Exception e) {
            // Log the error and return 500 Internal Server Error
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/jobDetails/{jobId}")
    public ResponseEntity<PostJobs> getJobDetails(@PathVariable Long jobId) {
        Optional<PostJobs> jobDetails = jobService.getJobDetails(jobId);

        if (jobDetails.isPresent()) {
            return ResponseEntity.ok(jobDetails.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/jobDelete/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        Optional<PostJobs> existingJob = jobService.getJobById(id);
        if (!existingJob.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Job not found
        }

        try {
            // Delete the job by its ID
            postJobsRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully deleted
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle error
        }
    }


    @GetMapping("/admin")
    public ResponseEntity<List<PostJobs>> getAllJobsForAdmin() {
        // Assuming we need to check if the user is an admin before proceeding
        // You can add some authentication check here, such as using Spring Security
        List<PostJobs> allJobs = postJobsRepository.findAll();

        if (allJobs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(allJobs, HttpStatus.OK);
    }


}
