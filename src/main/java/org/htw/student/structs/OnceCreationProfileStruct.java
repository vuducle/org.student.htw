package org.htw.student.structs;

import org.htw.student.models.BIAS;

public record OnceCreationProfileStruct(String username, String password, String confirmPassword, String email,
                                        BIAS bias, String bio, String imageUrl) {
}
