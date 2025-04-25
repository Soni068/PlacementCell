package net.javaguides.Placement_System.service;

import net.javaguides.Placement_System.Repository.TeamRepository;
import net.javaguides.Placement_System.entity.TeamMembers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    public TeamRepository teamRepository;

    public TeamMembers addTeamMember(TeamMembers teamMembers) {
        return teamRepository.save(teamMembers);
    }

    public List<TeamMembers> getAllTeamMembers() {
        return teamRepository.findAll();
    }

    public boolean deleteTeamMember(Long teamId) {
        if (teamRepository.existsById(teamId)) {
            teamRepository.deleteById(teamId);
            return true;
        }
        return false; // Or throw an exception if needed
    }

    public TeamMembers updateTeamMember(Long teamId, TeamMembers updatedTeamMember) {
        if (teamRepository.existsById(teamId)) {
            updatedTeamMember.setTeam_id(teamId);  // Ensure the ID stays the same
            return teamRepository.save(updatedTeamMember);
        }
        return null; // Or throw an exception if needed
    }

    public Optional<TeamMembers> getTeamMemberById(Long teamId) {
        return teamRepository.findById(teamId);
    }
}
