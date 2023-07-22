package br.com.bodegami.webfluxcourse.controler;

import br.com.bodegami.webfluxcourse.entity.User;
import br.com.bodegami.webfluxcourse.mapper.UserMapper;
import br.com.bodegami.webfluxcourse.model.request.UserRequest;
import br.com.bodegami.webfluxcourse.model.response.UserResponse;
import br.com.bodegami.webfluxcourse.service.UserService;
import br.com.bodegami.webfluxcourse.service.exception.ObjectNotFoundException;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    private String id;
    private String name;
    private String email;
    private String password;
    private String baseUri;

    @BeforeEach
    void setup() {
        id = "987654321";
        name = "renato";
        email = "renato@email.com";
        password = "123";
        baseUri = "/users";
    }


    @DisplayName("Test endpoint save with success")
    @Test
    void testSaveWithSuccess() {
        UserRequest request = new UserRequest(name, email, password);

        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .value(Objects::nonNull)
                .consumeWith(System.out::println);
        verify(service, times(1)).save(any(UserRequest.class));
    }

    @DisplayName("Test endpoint save with bad request")
    @Test
    void testSaveWithBadRequest() {
        name = "   renato    ";
        UserRequest request = new UserRequest(name, email, password);

        when(service.save(any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri(baseUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(baseUri)
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                .jsonPath("$.errors[0].message").isEqualTo("field cannot have blank spaces");

        verify(service, times(0)).save(any(UserRequest.class));
    }

    @DisplayName("Test find by id endpoint with success")
    @Test
    void testFindByIdWithSuccess() {
        UserResponse response = new UserResponse(id, name, email, password);

        when(service.findById(anyString())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(response);

        webTestClient.get().uri(baseUri + "/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo(name)
                .jsonPath("$.email").isEqualTo(email)
                .jsonPath("$.password").isEqualTo(password);

        verify(service, times(1)).findById(anyString());
        verify(mapper, times(1)).toResponse(any(User.class));
    }

    @DisplayName("Test find by id endpoint with not found")
    @Test
    void testFindByIdWithError() {
        UserResponse response = new UserResponse(id, name, email, password);
        String resourcePath = baseUri + "/" + id;

        when(service.findById(anyString())).thenThrow(new ObjectNotFoundException(
                String.format("Object not found. ID: %s - Type: %s", id, User.class.getSimpleName())));

        webTestClient.get().uri(resourcePath)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo(resourcePath)
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.error").isEqualTo("Not Found")
                .jsonPath("$.message").isEqualTo(
                        String.format("Object not found. ID: %s - Type: %s", id, User.class.getSimpleName()));

        verify(service, times(1)).findById(anyString());
    }

    @DisplayName("Test find all endpoint with success")
    @Test
    void testFindAllWithSuccess() {
        UserResponse response = new UserResponse(id, name, email, password);

        when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(response);

        webTestClient.get().uri(baseUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(id)
                .jsonPath("$.[0].name").isEqualTo(name)
                .jsonPath("$.[0].email").isEqualTo(email)
                .jsonPath("$.[0].password").isEqualTo(password);

        verify(service, times(1)).findAll();
        verify(mapper, times(1)).toResponse(any(User.class));
    }

    @DisplayName("Test update endpoint with success")
    @Test
    void update() {
        name = "Renato F.";
        String resourcePath = baseUri + "/" + id;
        UserRequest request = new UserRequest(name, email, password);
        UserResponse response = new UserResponse(id, name, email, password);

        when(service.update(anyString(), any(UserRequest.class))).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(response);

        webTestClient.patch().uri(resourcePath)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo(name)
                .jsonPath("$.email").isEqualTo(email)
                .jsonPath("$.password").isEqualTo(password)
                .consumeWith(System.out::println);
        verify(service, times(1)).update(anyString(), any(UserRequest.class));
        verify(mapper, times(1)).toResponse(User.builder().build());
    }

    @Test
    void delete() {
    }
}