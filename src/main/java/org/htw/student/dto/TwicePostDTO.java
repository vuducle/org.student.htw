package org.htw.student.dto;

import lombok.Getter;
import lombok.Setter;
import org.htw.student.models.TwicePost;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TwicePostDTO {
    private String id;
    private String title;
    private String content;
    private String memberName;
    private List<String> imageUrl;
    private LocalDateTime createdAt;
    private String onceUsername;
    private String onceProfileImageUrl;

    public TwicePostDTO(TwicePost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.memberName = post.getMemberName();
        this.imageUrl = post.getImageUrl();
        this.createdAt = post.getCreatedAt();
        this.onceUsername = post.getOnceUsername();
        this.onceProfileImageUrl = post.getOnceProfileImageUrl();
    }
}
