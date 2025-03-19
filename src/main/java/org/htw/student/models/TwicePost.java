package org.htw.student.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TwicePost {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String content;

    private String memberName;
    @ElementCollection
    private List<String> imageUrl = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "once_id", nullable = false)
    @JsonBackReference
    private Once once;

    private String onceUsername;

    @Column(name = "once_id_value")
    private String onceId;

    @PrePersist
    @PreUpdate
    public void populateOnceDetails() {
        System.out.println("populateOnceDetails() aufgerufen");
        if (once != null) {
            this.onceUsername = once.getUsername();
            this.onceId = once.getId();
            System.out.println("Username: " + onceUsername);
            System.out.println("Once ID: " + onceId);
        } else {
            System.out.println("Once ist null");
        }
    }

}