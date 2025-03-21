package org.htw.student.repository;

import org.htw.student.models.Once;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OnceRepository extends CrudRepository<Once, String> {
    Optional<Once> findOnceById(String id);
    Optional<Once> findOnceByUsername(String username);
    Optional<Once> findOnceByEmail(String email);
    List<Once> findByUsernameContainingIgnoreCase(String username);
    //Optional<Once> findOnceByAuthentificated(Once once);
}
