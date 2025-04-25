package net.javaguides.Placement_System.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "post_jobs")
public class PostJobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private String salary;
    @ElementCollection
    @CollectionTable(name = "job_courses", joinColumns = @JoinColumn(name = "post_jobs_id"))
    @Column(name = "course")
    private List<String> courses = new ArrayList<>();
    private String type;  // Full-time, Part-time, etc.
    private LocalDate deadline;
    private String skills;
    private double ugCgpa;
    private double pgCgpa;
    private String companyWebsite;

    @ManyToOne
    @JoinColumn(name = "company_name", referencedColumnName = "name")  // This will link to the 'name' column in the company table
    private CompanyModel company;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public double getUgCgpa() {
        return ugCgpa;
    }

    public void setUgCgpa(double ugCgpa) {
        this.ugCgpa = ugCgpa;
    }

    public double getPgCgpa() {
        return pgCgpa;
    }

    public void setPgCgpa(double pgCgpa) {
        this.pgCgpa = pgCgpa;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }
}

