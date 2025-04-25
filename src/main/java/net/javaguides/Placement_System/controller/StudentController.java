package net.javaguides.Placement_System.controller;

import jakarta.validation.Valid;
import net.javaguides.Placement_System.Repository.StudentRepository;
import net.javaguides.Placement_System.entity.AdminModel;
import net.javaguides.Placement_System.entity.CompanyModel;
import net.javaguides.Placement_System.entity.PostJobs;
import net.javaguides.Placement_System.entity.StudentModel;
import net.javaguides.Placement_System.service.EmailService;
import net.javaguides.Placement_System.service.JobService;
import net.javaguides.Placement_System.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
    private final StudentService studentService;
    private final EmailService emailService;


    @Autowired
    public StudentController(StudentService studentService, EmailService emailService) {
        this.studentService = studentService;
        this.emailService = emailService;
    }
    @Autowired
   // private StudentService studentService;
    private StudentRepository studentRepository;

    @Autowired
    private JobService jobService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody StudentModel studentModel){
        try{
            StudentModel newStudent = studentService.registerUser(studentModel);
            return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>("Error registering user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/studentsList")
    public List<StudentModel> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/studentDetails/{stud_id}")
    public ResponseEntity<?> getStudentDetails(@PathVariable String stud_id) {
        StudentModel student = studentService.getStudentDetails(stud_id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }

    @PostMapping("/studentLogin")
    public ResponseEntity<?> login(@RequestBody  StudentModel studentModel)
    {
        StudentModel student = studentService.login(studentModel.getEmail(), studentModel.getPassword());

        // Check if admin exists and validate the password
        if (student != null) {
            if (student.getEndOfSession() != null && LocalDate.now().isAfter(student.getEndOfSession())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your session has expired. Please contact the administration.");
            }
            LoginResponse response = new LoginResponse("Student login successful!", student.getName(), student.getStud_id());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
        }
    }

    @PostMapping("/updateMarks/{stud_id}")
    public ResponseEntity<?> updateMarks(@PathVariable String stud_id,
                              @RequestParam Double marks10,
                              @RequestParam Double marks12,
                              @RequestParam Double marksUg,
                              @RequestParam Double marksPg){
        /*StudentModel studentModel = studentRepository.findById(stud_id).orElseThrow(() -> new RuntimeException("student not found"));

        studentModel.setMarks10(marks10);
        studentModel.setMarks12(marks12);
        studentModel.setMarksUg(marksUg);
        studentModel.setMarksPg(marksPg);

       studentRepository.save(studentModel);

        return ResponseEntity.ok("marks updated Successfully!");*/

        String result = studentService.updateMarks(stud_id,marks10, marks12,marksUg,marksPg);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/uploadResume/{stud_id}")
    public ResponseEntity<String> uploadResume(@PathVariable String stud_id,
                                               @RequestParam("resume") MultipartFile resumeFile) {
        try {
            // Call the service method to handle the file upload
            String result = studentService.uploadResume(stud_id, resumeFile);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/uploadCertificates/{stud_id}")
    public ResponseEntity<String> uploadCertificates(@PathVariable String stud_id,
                                     @RequestParam("certificates") List<MultipartFile> certificates) {
        try {
            String result = studentService.uploadCertificates(stud_id, certificates);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



    @GetMapping("/uploads/Certificates/{filename}")
    public ResponseEntity<FileSystemResource> getCertificate(@PathVariable String filename) {
        String uploadCertDir = "D:/Internship/Placement-System/uploads/certificates";
        File file = new File(uploadCertDir + "/" + filename);

        if (file.exists()) {
            FileSystemResource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/uploads/Resumes/{filename}")
    public ResponseEntity<FileSystemResource> getResume(@PathVariable String filename) {
        // Define the directory where resumes are stored
        String uploadResumesDir = "D:/Internship/Placement-System/uploads/resume";  // Change this path according to your setup

        // Create a File object using the directory and filename
        File file = new File(uploadResumesDir + "/" + filename);

        if (file.exists()) {
            FileSystemResource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required!");
        }

        StudentModel student = studentService.findByEmail(email);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found in database!");
        }

        String token = studentService.generatePasswordResetToken(email);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating reset token.");
        }

        String subject = "Reset Your Password";
        String message = "<p>Dear Student,</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href='http://localhost:3000/reset-password?token=" + token + "'>Reset Password</a>"
                + "<p>This link is valid for 15 minutes.</p>"
                + "<p>Regards,</p><p><strong>Placement System Team</strong></p>";

        try {
            emailService.sendEmail(email, subject, message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }

        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (token == null || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("Token and new password are required!");
        }

        boolean isReset = studentService.resetPassword(token, newPassword);
        if (!isReset) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token!");
        }

        return ResponseEntity.ok("Password updated successfully!");
    }


    @GetMapping("/relevantJobs/{studId}")
    public ResponseEntity<List<PostJobs>> getRelevantJobs(@PathVariable String studId) {
        try {
            // Get relevant jobs for the student
            List<PostJobs> relevantJobs = jobService.getRelevantJobsForStudent(studId);

            if (relevantJobs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // No relevant jobs found
            }

            return new ResponseEntity<>(relevantJobs, HttpStatus.OK); // Return the relevant jobs
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle error
        }
    }

    @GetMapping("/notPlacedStudents")
    public ResponseEntity<List<StudentModel>> getNotPlacedStudents() {
        List<StudentModel> notPlacedStudents = studentService.getStudentsByStatus(StudentModel.Status.NOT_PLACED);
        if (notPlacedStudents.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(notPlacedStudents);
    }

    @GetMapping("/placedStudents")
    public ResponseEntity<List<StudentModel>> getPlacedStudents() {
        List<StudentModel> placedStudents = studentService.getStudentsByStatus(StudentModel.Status.PLACED);
        if (placedStudents.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(placedStudents);
    }
    @PutMapping("/updateProfile/{stud_id}")
    public ResponseEntity<String> updateProfile(@PathVariable String stud_id,
                                                @RequestBody StudentModel updatedStudent) {
        try {
            String result = studentService.updateProfile(stud_id, updatedStudent);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<StudentModel> updateStudentPassword(@RequestParam String email, @RequestParam String oldPassword,@RequestParam String newPassword) {
        StudentModel StudentModel = studentService.updateStudentPassword(email, oldPassword, newPassword);
        return ResponseEntity.ok(StudentModel);
    }



}
 class LoginResponse {
    private String message;
    private String name;
    private String stud_id;
    // Constructor
    public LoginResponse(String message, String name, String stud_id) {
        this.message = message;
        this.name = name;
        this.stud_id = stud_id;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
 }
