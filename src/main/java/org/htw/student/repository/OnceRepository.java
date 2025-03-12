package org.htw.student.repository;

import org.htw.student.models.Once;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OnceRepository extends CrudRepository<Once, String> {
    Optional<Once> findOnceById(String id);
    Optional<Once> findOnceByUsername(String username);
    Optional<Once> findOnceByEmail(String email);
    //Optional<Once> findOnceByAuthentificated(Once once);
}
