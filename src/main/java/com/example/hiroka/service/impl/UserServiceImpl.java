package com.example.hiroka.service.impl;

import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.repo.PodcastRepository;
import com.example.hiroka.repo.UserRepository;
import com.example.hiroka.service.UserService;
import com.example.hiroka.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PodcastRepository podcastRepository;


    public List<User> findAllUsers(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return userRepository.findAll();
        } else {
            return userRepository.search(filterText);
        }
    }


    public void deleteUser(User user){
        userRepository.delete(user);
    }


    public List<Podcast> findAllPodcast(){
        return podcastRepository.findAll();
    }

    public void saveUser(User user){
        if(user == null){
            System.out.println("not found user");
            return;
        }
        userRepository.save(user);
    }
}