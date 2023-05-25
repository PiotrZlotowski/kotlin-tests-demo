package com.demo.hackernews.service;

import com.demo.hackernews.model.Comment;
import com.demo.hackernews.model.Item;
import com.demo.hackernews.model.Post;
import com.demo.hackernews.model.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HackerNewsService {
    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    private final RestTemplate restTemplate;

    public HackerNewsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int[] getNewStories() {
        String url = BASE_URL + "newstories.json";
        return restTemplate.getForObject(url, int[].class);
    }

    public Item getItem(String id) {
        String url = BASE_URL + "item/" + id + ".json";
        return restTemplate.getForObject(url, Item.class);
    }

    public User getUser(String username) {
        String url = BASE_URL + "user/" + username + ".json";
        return restTemplate.getForObject(url, User.class);
    }

    public Set<Item> getTopStories(int minScore) {
        String url = BASE_URL + "topstories.json";
        return Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(url, int[].class)))
                .mapToObj(storyId -> getItem(String.valueOf(storyId)))
                .filter(item -> item.getScore() >= minScore)
                .collect(Collectors.toSet());
    }

    public String[] getTopTags() {
        String url = BASE_URL + "tags.json";
        return restTemplate.getForObject(url, String[].class);
    }

    public int[] getPostsByTag(String tag) {
        String url = BASE_URL + "tags/" + tag + ".json";
        return restTemplate.getForObject(url, int[].class);
    }

    public int addNewPost(Post post) {
        String url = BASE_URL + "item.json";
        HttpEntity<Post> request = new HttpEntity<>(post);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String location = response.getHeaders().getLocation().toString();
        String id = location.substring(location.lastIndexOf("/") + 1);
        return Integer.parseInt(id);
    }

    public int addNewComment(Comment comment) {
        String url = BASE_URL + "item.json";
        HttpEntity<Comment> request = new HttpEntity<>(comment);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String location = response.getHeaders().getLocation().toString();
        String id = location.substring(location.lastIndexOf("/") + 1);
        return Integer.parseInt(id);
    }

    public void upvotePostOrComment(int id) {
        String url = BASE_URL + "item/" + id + ".json";
        Item item = restTemplate.getForObject(url, Item.class);
        item.incrementScore();
        restTemplate.put(url, item);
    }

    public void downvotePostOrComment(int id) {
        String url = BASE_URL + "item/" + id + ".json";
        Item item = restTemplate.getForObject(url, Item.class);
        item.decrementScore();
        restTemplate.put(url, item);
    }

    public void deletePostOrComment(int id) {
        String url = BASE_URL + "item/" + id + ".json";
        restTemplate.delete(url);
    }
}