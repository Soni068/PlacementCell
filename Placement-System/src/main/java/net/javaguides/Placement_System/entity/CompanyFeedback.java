package net.javaguides.Placement_System.entity;

import jakarta.persistence.*;

@Entity
@Table(name="company_feedback")
public class CompanyFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to CompanyModel
    @ManyToOne
    @JoinColumn(name = "company_name", referencedColumnName = "name")
    private CompanyModel company;

    private String companyEmail;

    @Column(nullable = false, length = 1000)
    private String message;

    // Constructors
    public CompanyFeedback() {
    }

    public CompanyFeedback(CompanyModel company, String message) {
        this.company = company;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }


    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
