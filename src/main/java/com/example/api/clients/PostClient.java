package com.example.api.clients;

import com.example.api.core.ApiClient;
import com.example.api.core.FluentResponse;
import com.example.api.models.Post;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class PostClient extends ApiClient {

    public FluentResponse<Post> getPost(int id) {

        Response response = get("/posts/" + id);

        return (FluentResponse<Post>) FluentResponse.of(response);
    }

    public FluentResponse<List<Post>> getPosts() {

        Response response = get("/posts");

        return (FluentResponse<List<Post>>) FluentResponse.of(response);
    }

    public FluentResponse<Post> createPost(Post post) {

        Response response = post("/posts", post);

        return (FluentResponse<Post>) FluentResponse.of(response);
    }

    public FluentResponse<Post> putPost(int id, Post post) {

        Response response = put("/posts/" + id, post);

        return (FluentResponse<Post>) FluentResponse.of(response);
    }

    public FluentResponse<Post> deletePost(int id) {

        Response response = delete("/posts/" + id);

        return (FluentResponse<Post>) FluentResponse.of(response);
    }

    public FluentResponse<Post> patchPost(int id, Map<String, Object> patchBody) {
        Response response = patch("/posts/" + id, patchBody);
        return (FluentResponse<Post>) FluentResponse.of(response);
    }
}
