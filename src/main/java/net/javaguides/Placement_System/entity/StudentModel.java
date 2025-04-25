package net.javaguides.Placement_System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.time.LocalDate;



@Entity
@Table(name="student_table")
public class StudentModel {

    private String name;

    @Id
    @Pattern(regexp = "^[A-Z]{5}\\d{5}$", message = "Please enter the correct student id")
    private String stud_id;

    private String course;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(name="phone", unique = true)
    private String phone;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+@banasthali\\.in$", message = "Invalid email format")
    @Column(name="email", unique = true)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$", message = "Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, a number, and a special character.")
    @Size(min = 8)
    private String password;




    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOfSession;


    private Double marks10;

    private Double marks12;

    private Double marksUg;

    private Double marksPg;

    private String resumePath;

    //@ElementCollection
    private List<String> certificatePaths;

    @Enumerated(EnumType.STRING)
    private Status status1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public LocalDate getEndOfSession() {
        return endOfSession;
    }

    public void setEndOfSession(LocalDate endOfSession) {
        this.endOfSession = endOfSession;
    }

    public Double getMarks10() {
        return marks10;
    }

    public void setMarks10(Double marks10) {
        this.marks10 = marks10;
    }

    public Double getMarks12() {
        return marks12;
    }

    public void setMarks12(Double marks12) {
        this.marks12 = marks12;
    }

    public Double getMarksUg() {
        return marksUg;
    }

    public void setMarksUg(Double marksUg) {
        this.marksUg = marksUg;
    }

    public Double getMarksPg() {
        return marksPg;
    }

    public void setMarksPg(Double marksPg) {
        this.marksPg = marksPg;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public List<String> getCertificatePaths() {
        return certificatePaths;
    }

    public void setCertificatePaths(List<String> certificatePaths) {
        this.certificatePaths = certificatePaths;
    }

    public Status getStatus1() {
        return status1;
    }

    public void setStatus1(Status status1) {
        this.status1 = status1;
    }

    public enum Status {
        PLACED,
        NOT_PLACED
    }
}
