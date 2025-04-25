package net.javaguides.Placement_System.service;

import net.javaguides.Placement_System.Repository.PostJobsRepository;
import net.javaguides.Placement_System.Repository.StudentRepository;
import net.javaguides.Placement_System.entity.CompanyModel;
import net.javaguides.Placement_System.entity.PostJobs;
import net.javaguides.Placement_System.entity.StudentModel;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentService {
    @Autowired
    public StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ConcurrentHashMap<String, TokenEntry> tokenStorage = new ConcurrentHashMap<>();


    static class TokenEntry {
        private final String token;
        private final LocalDateTime expiryTime;

        public TokenEntry(String token, LocalDateTime expiryTime) {
            this.token = token;
            this.expiryTime = expiryTime;
        }

        public String getToken() {
            return token;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }

    public StudentModel registerUser(StudentModel studentModel)
    {
        studentModel.setStatus1(StudentModel.Status.NOT_PLACED);
        studentModel.setPassword(passwordEncoder.encode(studentModel.getPassword()));

        return studentRepository.save(studentModel);
    }

    public StudentModel updateStudentPassword(String email, String oldPassword, String newPassword) {
        StudentModel studentDetails = studentRepository.findByEmail(email);
        String encodedPass = passwordEncoder.encode(oldPassword);
        if(passwordEncoder.matches(oldPassword, studentDetails.getPassword())){
            studentRepository.updateStudentPass(email, passwordEncoder.encode(newPassword));
            return studentRepository.findByEmail(email);
        }
        return null;
    }

    // ✅ Login method with password verification
    public StudentModel login(String email, String password) {
        StudentModel student = studentRepository.findByEmail(email);
        if (student != null && passwordEncoder.matches(password, student.getPassword())) {
            return student; // Correct credentials
        }
        return null; // Invalid credentials
    }

    public List<StudentModel> getAllStudents(){
        return studentRepository.findAll();
    }

    public StudentModel getStudentDetails(String stud_id) {
        return studentRepository.findById(stud_id).orElse(null); // Fetch student by ID
    }

    public String updateMarks(String stud_id, Double marks10, Double marks12, Double marksUg, Double marksPg) {
        int updatedRows = studentRepository.updateMarks(marks10, marks12, marksUg, marksPg, stud_id);
        if (updatedRows > 0) {
            return "Marks updated successfully!";
        } else {
            return "Student not found or update failed.";
        }
    }
    @Value("${upload.resumes.dir}")
    private String uploadDir;  // Directory where the resumes will be stored

    // ✅ Utility method to ensure directories exist
    private void ensureDirectoryExists(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String uploadResume(String stud_id, MultipartFile resumeFile) {
        // Generate a unique file name using the student ID and timestamp
        String fileName = stud_id + "_" + System.currentTimeMillis() + "_" + resumeFile.getOriginalFilename();

        // Path to save the resume
        Path filePath = Paths.get(uploadDir, fileName);

        // Ensure the upload directory exists
        ensureDirectoryExists(uploadDir);

        try {
            // Save the file to the server directory
            Files.copy(resumeFile.getInputStream(), filePath);

            // Save the file path in the database
            int rowsUpdated = studentRepository.uploadResume(fileName, stud_id);

            if (rowsUpdated > 0) {
                return "Resume uploaded and path updated successfully!";
            } else {
                return "Student not found or failed to update resume path.";
            }
        } catch (IOException e) {
            return "Error saving resume file: " + e.getMessage();
        }
    }

    @Value("${upload.certificates.dir}")
    private String uploadCertDir;

    // Method to upload certificates
    public String uploadCertificates(String stud_id, List<MultipartFile> certificateFiles) {
        List<String> certificatePaths = new ArrayList<>();

        // Ensure the upload directory exists
        ensureDirectoryExists(uploadCertDir);

        try {

            StudentModel student = studentRepository.findById(stud_id).orElseThrow(() -> new RuntimeException("Student not found"));

            // Get existing certificate paths from the student entity
            List<String> existingCertificatePaths = student.getCertificatePaths();

            for (MultipartFile certificateFile : certificateFiles) {
                // Generate a unique file name using the student ID, timestamp, and original file name
                String fileName = stud_id + "_cert_" + System.currentTimeMillis() + "_" + certificateFile.getOriginalFilename();

                // Path to save the certificate
                Path filePath = Paths.get(uploadCertDir, fileName);

                //certificateFile.transferTo(filePath.toFile());
                // Save the file to the server directory
               Files.copy(certificateFile.getInputStream(), filePath);

                // Add the file path to the list
                certificatePaths.add(fileName);
            }

            if (existingCertificatePaths != null && !existingCertificatePaths.isEmpty()) {
                certificatePaths.addAll(existingCertificatePaths); // Add existing certificates first
            }

            // Update the student record with the list of certificate paths
            int rowsUpdated = studentRepository.uploadCertificates(certificatePaths, stud_id);

            if (rowsUpdated > 0) {
                return "Certificates uploaded and paths updated successfully!";
            } else {
                return "Student not found or failed to update certificate paths.";
            }

        } catch (IOException e) {
            return "Error saving certificate files: " + e.getMessage();
        }
    }
    // ✅ Find student by email
    public StudentModel findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    // ✅ Generate token for forgot password
    public String generatePasswordResetToken(String email) {
        StudentModel student = studentRepository.findByEmail(email);
        if (student == null) {
            System.out.println("⚠️ Student with email " + email + " not found!");
            return null;
        }

        String token = UUID.randomUUID().toString();
        tokenStorage.put(email, new TokenEntry(token, LocalDateTime.now().plusMinutes(15)));

        System.out.println("✅ Token generated for " + email + ": " + token);
        return token;
    }


    // ✅ Validate token and update password
    public boolean resetPassword(String token, String newPassword) {
        for (Map.Entry<String, TokenEntry> entry : tokenStorage.entrySet()) {
            if (entry.getValue().getToken().equals(token) && entry.getValue().getExpiryTime().isAfter(LocalDateTime.now())) {
                StudentModel student = studentRepository.findByEmail(entry.getKey());
                if (student != null) {
                    student.setPassword(passwordEncoder.encode(newPassword)); // Hash new password
                    studentRepository.save(student);
                    tokenStorage.remove(entry.getKey()); // Remove token after use
                    return true;
                }
            }
        }
        return false; // Invalid or expired token
    }



    public List<StudentModel> getStudentsByStatus(StudentModel.Status status) {
        return studentRepository.findByStatus1(status); // Assuming you have a method in your repository
    }

    public String updateProfile(String stud_id, StudentModel updatedStudent) {
        StudentModel student = studentRepository.findById(stud_id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update all fields
        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setPhone(updatedStudent.getPhone());
        student.setCourse(updatedStudent.getCourse());
        student.setMarks10(updatedStudent.getMarks10());
        student.setMarks12(updatedStudent.getMarks12());
        student.setMarksUg(updatedStudent.getMarksUg());
        student.setMarksPg(updatedStudent.getMarksPg());
        student.setEndOfSession(updatedStudent.getEndOfSession());
        studentRepository.save(student);
        return "Profile updated successfully!";
    }

}
