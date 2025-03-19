package org.htw.student.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TwicePost {
    private static final Logger log = LoggerFactory.getLogger(TwicePost.class);
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
    private String onceProfileImageUrl;

    @Column(name = "once_id_value")
    private String onceId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TwiceComment> comments = new ArrayList<>();

    // Upvotes (einfache Liste von Once-IDs)
    @ElementCollection
    private List<String> upvotedBy = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void populateOnceDetails() {
        log.info("populateOnceDetails() aufgerufen");
        if (once != null) {
            this.onceUsername = once.getUsername();
            this.onceProfileImageUrl = once.getImageUrl();
            this.onceId = once.getId();
            log.info("Username: " + onceUsername);
            log.info("Once ID: " + onceId);
        } else {
            log.error("Once ist null");
        }
    }

}