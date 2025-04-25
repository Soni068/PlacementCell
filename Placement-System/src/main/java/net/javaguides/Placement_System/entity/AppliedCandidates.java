package net.javaguides.Placement_System.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applied_candidates")
public class AppliedCandidates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    // Relationship with Student
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "stud_id")
    private StudentModel student;

    // Relationship with PostJobs
    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private PostJobs job;

    // Redundant but useful: snapshot fields
    private String studentName;
    private String companyName;
    private String jobTitle;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public PostJobs getJob() {
        return job;
    }

    public void setJob(PostJobs job) {
        this.job = job;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
