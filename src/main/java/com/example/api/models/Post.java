package com.example.api.models;

import lombok.*;

@Getter
@Setter
public class Post {
    private Integer userId;
    private Integer id;
    private String title;
    private String body;
}
