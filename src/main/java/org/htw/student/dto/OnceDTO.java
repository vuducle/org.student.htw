package org.htw.student.dto;

import lombok.Getter;
import lombok.Setter;
import org.htw.student.models.BIAS;
import org.htw.student.models.Once;
import org.htw.student.models.TwicePost;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OnceDTO {

    private String id;
    private String username;
    private String imageUrl;
    private String bio;
    private BIAS bias;
    private final List<TwicePost> twicePost;
    //private List<TwicePost> twicePosts;

    public OnceDTO(Once once) {
        this.id = once.getId();
        this.username = once.getUsername();
        this.imageUrl = once.getImageUrl();
        this.bio = once.getBio();
        this.bias = once.getBias();
        this.twicePost = once.getTwicePosts();
    }
}
