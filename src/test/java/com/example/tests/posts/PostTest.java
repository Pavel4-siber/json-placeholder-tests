package com.example.tests.posts;

import com.example.api.builders.PostBuilder;
import com.example.api.clients.PostClient;
import com.example.api.models.Post;
import com.example.tests.extensions.RetryExtension;
import com.example.tests.extensions.TestDataExtension;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static com.example.api.core.CoverageTracker.*;
import static com.example.api.core.CoverageTracker.reset;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({TestDataExtension.class, RetryExtension.class})
public class PostTest {

    PostClient postClient = new PostClient();

    @BeforeAll
    static void initCoverage() {
        loadOpenAPISpec("src/main/resources/openapi.yaml");
    }

    @Test
    void shouldGetPost() {

        postClient
                .getPost(1)
                .status(200)
                .schema("post-schema.json")
                .body(Post.class)
                .assertThat(p -> {
                    assertThat(p.getId()).isEqualTo(1);
                    assertThat(p.getTitle()).isNotBlank();
                });
    }

    @Test
    void shouldGetPosts() {

        postClient
                .getPosts()
                .status(200)
                .body(new TypeRef<List<Post>>() {
                })
                .assertThat(p -> {
                    assertThat(p).isNotEmpty().hasSizeGreaterThan(10);
                    assertThat(p).extracting(Post::getUserId).hasSize(100);
                });
    }

    @Test
    void shouldCreatePost() {

        Post post = PostBuilder
                .defaultPost()
                .withTitle("API Test")
                .build();

        postClient
                .createPost(post)
                .status(201)
                .body(Post.class)
                .assertThat(p -> {
                    assertThat(p.getTitle()).isEqualTo("API Test");
                    assertThat(p.getId()).isNotNull();
                });
    }

    @Test
    void shouldPutPost() {

        Post post = PostBuilder
                .defaultPost()
                .withTitle("Update title")
                .build();

        postClient
                .putPost(1, post)
                .status(200)
                .body(Post.class)
                .assertThat(p -> {
                     assertThat(p.getTitle()).isEqualTo("Update title");
                });
    }

    @Test
    void shouldDeletePost() {
        postClient
                .deletePost(1)
                .status(200);
    }

    @Test
    void shouldPatchPost() {

        Map<String, Object> patchBody = Map.of(
                "title", "Patched title"
        );

        postClient
                .patchPost(1, patchBody)
                .status(200)
                .body(Post.class)
                .assertThat(p -> assertThat(p.getTitle()).isEqualTo("Patched title"));

    }

    @Test
    void shouldMatchPostSchema() {

        postClient
                .getPost(1)
                .status(200)
                .schema("post-schema.json");
    }

    @Test
    void shouldReturn404ForUnknownPost() {

        postClient
                .getPost(99999)
                .status(404);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void shouldGetPosts(int id) {

        postClient
                .getPost(id)
                .status(200)
                .body(Post.class)
                .assertThat(p ->
                    assertThat(p.getId()).isEqualTo(id));
    }

    @AfterAll
    static void tearDown() {
        attachAllureSummary();
        Map<String, Object> jsonReport = getCoverageJson();
        System.out.println("CI/CD JSON coverage: " + jsonReport);
        reset();
    }
}
