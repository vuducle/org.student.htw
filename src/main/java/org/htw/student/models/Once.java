package org.htw.student.models;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
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
    private List<TwicePost> twicePosts = new ArrayList<>();
}
