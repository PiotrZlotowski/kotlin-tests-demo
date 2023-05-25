package com.demo.hackernews;

import com.demo.hackernews.model.Item;
import com.demo.hackernews.model.User;
import com.demo.hackernews.service.HackerNewsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@Log4j2
public class HackerNewsController {

    private final HackerNewsService hackerNewsService;

    public HackerNewsController(HackerNewsService hackerNewsService) {
        this.hackerNewsService = hackerNewsService;
    }

//    @GetMapping("/new-stories")
//    public ResponseEntity<int[]> getNewStories() {
//        return ResponseEntity.ok(hackerNewsService.getTopStories());
//    }

    @GetMapping("/items")
    public ResponseEntity<Set<Item>> getTopStories() {
        var topStories = hackerNewsService.getTopStories(200);
        log.info("Top stories {}", topStories);
        return ResponseEntity.ok(topStories);
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable String itemId) {
        Item item = hackerNewsService.getItem(itemId);
        log.info("Got the item {}", item);
        return ResponseEntity.ok(item);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = hackerNewsService.getUser(userId);
        log.info("Got the user {}", user);
        return ResponseEntity.ok(user);
    }
}