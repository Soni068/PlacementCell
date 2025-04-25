package net.javaguides.Placement_System.controller;


import net.javaguides.Placement_System.entity.AppliedCandidates;
import net.javaguides.Placement_System.service.AppliedCandidatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/applied")
@CrossOrigin(origins = "http://localhost:3000")
public class AppliedCandidatesController {

    @Autowired
    private AppliedCandidatesService appliedService;

    @PostMapping("/apply")
    public AppliedCandidates applyToJob(@RequestParam String studentId, @RequestParam Long jobId) {
        return appliedService.applyToJob(studentId, jobId);
    }




    @GetMapping
    public List<AppliedCandidates> getAllApplications() {
        return appliedService.getAllApplications();
    }

    @GetMapping("/check")
    public boolean checkIfApplied(@RequestParam String studentId, @RequestParam Long jobId) {
        // Calling service layer to check if the student has applied for the job
        return appliedService.isCandidateAppliedForJob(studentId, jobId);
    }

    @GetMapping("/by-job/{jobId}")
    public List<AppliedCandidates> getCandidatesByJobId(@PathVariable Long jobId) {
        return appliedService.getCandidatesByJobId(jobId);
    }


}
