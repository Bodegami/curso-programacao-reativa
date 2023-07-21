package br.com.bodegami.webfluxcourse.service;

import br.com.bodegami.webfluxcourse.entity.User;
import br.com.bodegami.webfluxcourse.mapper.UserMapper;
import br.com.bodegami.webfluxcourse.model.request.UserRequest;
import br.com.bodegami.webfluxcourse.repository.UserRepository;
import br.com.bodegami.webfluxcourse.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper =mapper;
    }

    public Mono<User> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }

    public Mono<User> findById(final String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ObjectNotFoundException(
                        String.format("Object not found. ID: %s - Type: %s", id, User.class.getSimpleName()))
                ));
    }



}
