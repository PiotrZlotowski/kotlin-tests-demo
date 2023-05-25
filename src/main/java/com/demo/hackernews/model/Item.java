package com.demo.hackernews.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {
    private int id;
    private String title;
    private String text;
    private String url;
    private int score;
    private int[] kids;
    private int parent;
    private String by;
    private long time;
    private String type;

    // getters and setters

    public void incrementScore() {
        this.score++;
    }

    public void decrementScore() {
        this.score--;
    }
}