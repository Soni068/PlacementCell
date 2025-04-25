package net.javaguides.Placement_System.controller;

import jakarta.validation.Valid;
import net.javaguides.Placement_System.entity.TeamMembers;
import net.javaguides.Placement_System.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/team")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    @Autowired
    public TeamService teamService;

    @PostMapping("/create")
    public ResponseEntity<TeamMembers> createTeamMember(
         @Valid   @RequestParam("tea_name") String tea_name,
            @RequestParam("tea_email") String tea_email,
            @RequestParam("tea_phone") String tea_phone,
            @RequestParam("tea_post") String tea_post,
            @RequestParam(value = "tea_image", required = false) MultipartFile tea_image) throws IOException {

        TeamMembers teamMember = new TeamMembers();
        teamMember.setTea_name(tea_name);
        teamMember.setTea_email(tea_email);
        teamMember.setTea_phone(tea_phone);
        teamMember.setTea_post(tea_post);

        // Handle the image if provided
        if (tea_image != null && !tea_image.isEmpty()) {
            teamMember.setTea_image(tea_image.getBytes());
        }

        // Save the team member with or without the image
        TeamMembers savedTeamMember = teamService.addTeamMember(teamMember);
        return new ResponseEntity<>(savedTeamMember, HttpStatus.CREATED);
    }



    @GetMapping("/all")
    public ResponseEntity<List<TeamMembers>> getAllTeamMembers() {
        List<TeamMembers> teamMembers = teamService.getAllTeamMembers();

        // Encode image to base64 for each team member
        for (TeamMembers member : teamMembers) {
            if (member.getTea_image() != null) {
                // Convert image to base64 string
                String base64Image = Base64.getEncoder().encodeToString(member.getTea_image());
                member.setTea_imageBase64(base64Image); // Set the base64 image string
            }
        }
        return new ResponseEntity<>(teamMembers, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable Long teamId) {
        Optional<TeamMembers> existingTeamMember = teamService.getTeamMemberById(teamId);

        if (existingTeamMember.isPresent()) {
            teamService.deleteTeamMember(teamId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{teamId}")
    public ResponseEntity<TeamMembers> updateTeamMember(@PathVariable Long teamId,
                                                        @Valid @RequestParam("tea_name") String tea_name,
                                                        @RequestParam("tea_email") String tea_email,
                                                        @RequestParam("tea_phone") String tea_phone,
                                                        @RequestParam("tea_post") String tea_post,
                                                        @RequestParam(value = "tea_image", required = false) MultipartFile tea_image) throws IOException {
        Optional<TeamMembers> existingTeamMember = teamService.getTeamMemberById(teamId);

        if (existingTeamMember.isPresent()) {
            TeamMembers teamMember = existingTeamMember.get();

            // Update team member details
            teamMember.setTea_name(tea_name);
            teamMember.setTea_email(tea_email);
            teamMember.setTea_phone(tea_phone);
            teamMember.setTea_post(tea_post);

            // Handle image if provided
            if (tea_image != null && !tea_image.isEmpty()) {
                teamMember.setTea_image(tea_image.getBytes());
            }

            TeamMembers updatedTeamMember = teamService.addTeamMember(teamMember); // Save updated data
            return new ResponseEntity<>(updatedTeamMember, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamMembers> getTeamMemberById(@PathVariable Long teamId) {
        Optional<TeamMembers> teamMember = teamService.getTeamMemberById(teamId);
        if (teamMember.isPresent()) {
            TeamMembers member = teamMember.get();

            // Convert image to base64 string if it exists
            if (member.getTea_image() != null) {
                String base64Image = Base64.getEncoder().encodeToString(member.getTea_image());
                member.setTea_imageBase64(base64Image); // Set the base64 image string in the model
            }
            return new ResponseEntity<>(member, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // If no team member is found with the provided ID
    }
}
