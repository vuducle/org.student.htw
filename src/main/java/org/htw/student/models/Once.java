package org.htw.student.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Once {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String username;
    @Column(nullable = true)
    private String email;
    @Enumerated
    private BIAS bias;
    private String hashPassword;
    @Column(length = 42069)
    private String bio;

    @Column(length = 500)
    private String imageUrl;
    @Enumerated
    private ALBUMS albums;
    @OneToMany(mappedBy = "once", cascade = CascadeType.ALL, orphanRemoval = true)

    @JsonManagedReference
    @JsonIgnore
    private List<TwicePost> twicePosts = new ArrayList<>();
}
