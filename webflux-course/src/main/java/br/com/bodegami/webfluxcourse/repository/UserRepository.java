package br.com.bodegami.webfluxcourse.repository;

import br.com.bodegami.webfluxcourse.entity.User;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public UserRepository(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Mono<User> save(final User user) {
        return reactiveMongoTemplate.save(user);
    }


}
