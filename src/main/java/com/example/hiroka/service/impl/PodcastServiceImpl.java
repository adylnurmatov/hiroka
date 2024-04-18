package com.example.hiroka.service.impl;

import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.repo.PodcastRepository;
import com.example.hiroka.service.PodcastService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PodcastServiceImpl implements PodcastService {
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
        Podcast save = new Podcast();
        save.setAuthor(podcast.getAuthor());
        save.setCategory(podcast.getCategory());
        save.setTitle(podcast.getTitle());
        save.setDescription(podcast.getDescription());
        save.setSubtitle(podcast.getSubtitle());
        save.setFileName(podcast.getFileName());
        podcastRepository.save(save);
    }

    public static class TextResponse {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


}