package com.example.hiroka.service.impl;

import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.repo.PodcastRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PodcastService {
    private final PodcastRepository podcastRepository;

    public List<Podcast> findAllPodcast(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return podcastRepository.findAll();
        } else {
            return podcastRepository.search(filterText);
        }
    }


    public void deletePodcast(Podcast contact){
        podcastRepository.delete(contact);
    }

    public void savePodcast(Podcast podcast){
    }


}