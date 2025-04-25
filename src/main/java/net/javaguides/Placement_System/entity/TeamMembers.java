package net.javaguides.Placement_System.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="Team_table")
public class TeamMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long team_id;

    private String tea_name;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = "Invalid email address format")
    @Column(name="tea_email", unique = true)
    private String tea_email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @Column(name="tea_phone", unique = true)
    private String tea_phone;

    private String tea_post;

    @Lob
    @Column(name = "tea_image")
    private byte[] tea_image;

    private String tea_imageBase64;

    public Long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Long team_id) {
        this.team_id = team_id;
    }

    public String getTea_name() {
        return tea_name;
    }

    public void setTea_name(String tea_name) {
        this.tea_name = tea_name;
    }

    public String getTea_email() {
        return tea_email;
    }

    public void setTea_email(String tea_email) {
        this.tea_email = tea_email;
    }

    public String getTea_phone() {
        return tea_phone;
    }

    public void setTea_phone(String tea_phone) {
        this.tea_phone = tea_phone;
    }

    public String getTea_post() {
        return tea_post;
    }

    public void setTea_post(String tea_post) {
        this.tea_post = tea_post;
    }

    public byte[] getTea_image() {
        return tea_image;
    }

    public void setTea_image(byte[] tea_image) {
        this.tea_image = tea_image;
    }

    public String getTea_imageBase64() {
        return tea_imageBase64;
    }

    public void setTea_imageBase64(String tea_imageBase64) {
        this.tea_imageBase64 = tea_imageBase64;
    }
}
