package org.htw.student.repository;

import org.htw.student.models.Once;
import org.htw.student.models.TwicePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TwicePostRepository extends JpaRepository<TwicePost, String> {
    List<TwicePost> findTwicePostById(String id);
    List<TwicePost> findByMemberName(String memberName);
    // ???
    //Map<Once, ArrayList<String>> getPostsFromOncie(Once once, List<?> twicePosts);

}
