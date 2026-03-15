package com.example.api.builders;

import com.example.api.models.Post;

public class PostBuilder {
    private Integer userId = 1;
    private String title = "Default title";
    private String body = "Default body";

    public static PostBuilder defaultPost() {
        return new PostBuilder();
    }

    public PostBuilder withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public PostBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PostBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public Post build() {

        Post post = new Post();
        // Id is null
        post.setUserId(userId);
        post.setTitle(title);
        post.setBody(body);

        return post;
    }

}
