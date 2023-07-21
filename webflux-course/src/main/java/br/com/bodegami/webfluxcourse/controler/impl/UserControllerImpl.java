package br.com.bodegami.webfluxcourse.controler.impl;

import br.com.bodegami.webfluxcourse.controler.UserController;
import br.com.bodegami.webfluxcourse.mapper.UserMapper;
import br.com.bodegami.webfluxcourse.model.request.UserRequest;
import br.com.bodegami.webfluxcourse.model.response.UserResponse;
import br.com.bodegami.webfluxcourse.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserControllerImpl(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Mono<Void>> save(UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> findById(String id) {
        Mono<UserResponse> response = service.findById(id).map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        Flux<UserResponse> response = service.findAll().map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return null;
    }
}
