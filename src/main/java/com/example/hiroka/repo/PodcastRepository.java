package com.example.hiroka.repo;

import com.example.hiroka.podcast.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    @Query("select p from Podcast p " +
            "where lower(p.title) like lower(concat('%', :searchTerm, '%')) ")
    List<Podcast> search(@Param("searchTerm") String searchTerm);
}
