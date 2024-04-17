package com.example.hiroka.service;

import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.user.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers(String filterText);
    List<Podcast> findAllPodcast();

    void deleteUser(User user);

    void saveUser(User user);
}
