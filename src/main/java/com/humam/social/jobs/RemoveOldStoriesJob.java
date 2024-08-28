package com.humam.social.jobs;

import com.humam.social.entity.Post;
import com.humam.social.repository.PostRepository;
import com.humam.social.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class RemoveOldStoriesJob {
    @Autowired
    private PostService postService;

    @Scheduled(cron = "0 0 * * * ?")
    public void removeExpiredStories() {
        postService.removeExpiredStories();
    }
}
