package com.demo.hackernews.model;


import lombok.Data;

@Data
public class User {
    private String id;
    private int karma;
    private String[] submitted;
}