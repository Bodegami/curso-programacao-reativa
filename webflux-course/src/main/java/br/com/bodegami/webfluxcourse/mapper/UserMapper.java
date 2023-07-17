package br.com.bodegami.webfluxcourse.mapper;

import br.com.bodegami.webfluxcourse.entity.User;
import br.com.bodegami.webfluxcourse.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(final UserRequest request);

}
