package org.htw.student.structs;

import java.time.LocalDateTime;

public record TwicePostCreationStruct(String title, String content, String memberName, String imageUrl, LocalDateTime createdAt, String username) {
}
