package br.com.bodegami.webfluxcourse.service;

import br.com.bodegami.webfluxcourse.entity.User;
import br.com.bodegami.webfluxcourse.mapper.UserMapper;
import br.com.bodegami.webfluxcourse.model.request.UserRequest;
import br.com.bodegami.webfluxcourse.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void save() {
        UserRequest request = new UserRequest("Renato", "renato@email.com", "12345");
        User entity = User.builder().build();

        when(mapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(repository.save(any(User.class))).thenReturn(Mono.just(entity));

        Mono<User> result = service.save(request);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        verify(repository, times(1)).save(any(User.class));
    }
}