package br.com.bodegami.webfluxcourse.model.request;

public record UserRequest(
        String name,
        String email,
        String password
) {
}
