package ds.hua.military.exemptions.models;


import ds.hua.military.exemptions.models.enums.Reason;
import ds.hua.military.exemptions.models.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;


@Entity
@Table
@Data
public class PostponementApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private Reason reason;

    @Column(name = "mil_number", nullable = false, unique = true)
    private Long milNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false,columnDefinition = "varchar(32) default 'PENDING'")
    private Status status = Status.PENDING;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "citizen_id", nullable = false)
    private User citizen;

    @Column(name = "file", nullable = true)
    private String file;

    @PrePersist
    public void prePersist() {
        LocalDateTime ldt = LocalDateTime.now();
        this.createdAt = new Date(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ldt));
    }
}
