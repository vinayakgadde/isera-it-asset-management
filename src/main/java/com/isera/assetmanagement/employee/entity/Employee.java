package com.isera.assetmanagement.employee.entity;

import com.isera.assetmanagement.department.entity.Department;
import com.isera.assetmanagement.location.entity.Location;
import com.isera.assetmanagement.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_employees_code",
                        columnNames = "employee_code"
                ),
                @UniqueConstraint(
                        name = "uk_employees_email",
                        columnNames = "email"
                )
        }
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "employee_code",
            nullable = false,
            length = 50
    )
    private String employeeCode;

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            length = 100
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            length = 150
    )
    private String email;

    @Column(
            name = "phone",
            length = 30
    )
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "department_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_employees_department"
            )
    )
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "location_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_employees_location"
            )
    )
    private Location location;

    @Column(
            name = "designation",
            length = 100
    )
    private String designation;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private String status = "ACTIVE";

    /*
     * Application login account associated
     * with this employee.
     *
     * Nullable because not every employee
     * must have application access.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_employees_user"
            )
    )
    private User user;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(
            String employeeCode
    ) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(
            String firstName
    ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(
            String lastName
    ) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone
    ) {
        this.phone = phone;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(
            Department department
    ) {
        this.department = department;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(
            Location location
    ) {
        this.location = location;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(
            String designation
    ) {
        this.designation = designation;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(
            LocalDate joiningDate
    ) {
        this.joiningDate = joiningDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(
            User user
    ) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}