package org.htw.student.dto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OnceSessionDTO {
    private final String id;
    private final String username;

    public OnceSessionDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
