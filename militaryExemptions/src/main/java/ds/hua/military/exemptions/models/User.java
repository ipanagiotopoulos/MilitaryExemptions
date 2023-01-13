package ds.hua.military.exemptions.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ds.hua.military.exemptions.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Table
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank
    @Size(min = 5, message = "Username must have at least 5 characters")
    @Column(nullable = false, unique = true)
    String username;

    @NotBlank
    @Size(min = 3, message = "First name must have at least 3 characters")
    @Column(name = "first_name", nullable = false)
    String firstName;

    @NotBlank
    @Size(min = 5, message = "Last name must have at least 5 characters")
    @Column(name = "last_name", nullable = false)
    String lastName;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Column(name = "password",nullable = false)
    @Size(min = 6, message = "Password must have at least 5 characters")
    String password;

    @JsonIgnore
    @Transient
    String confirmPassword;

    @Transient
    String full_name;

    @Column(columnDefinition = "boolean default true", nullable = false)
    boolean enabled = true;

    @Column(length = 32, columnDefinition = "varchar(32) default 'ROLE_CITIZEN'",nullable=false)
    @Enumerated(EnumType.STRING)
    Role role;

    public String getFull_name() {
        return firstName+" "+lastName;
    }
}
