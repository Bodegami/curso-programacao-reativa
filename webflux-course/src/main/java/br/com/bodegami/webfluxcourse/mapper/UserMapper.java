package br.com.bodegami.webfluxcourse.mapper;

import br.com.bodegami.webfluxcourse.entity.User;
import br.com.bodegami.webfluxcourse.model.request.UserRequest;
import br.com.bodegami.webfluxcourse.model.response.UserResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request, @MappingTarget final User entity);

    UserResponse toResponse(final User entity);

}
