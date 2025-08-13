package ru.java.filesharing.web.mapper;

import org.mapstruct.Mapper;
import ru.java.filesharing.entity.user.User;
import ru.java.filesharing.web.dto.user.request.CreateUserRequest;
import ru.java.filesharing.web.dto.user.request.UpdateUserRequest;
import ru.java.filesharing.web.dto.user.response.CreateUserResponse;
import ru.java.filesharing.web.dto.user.response.GetUserResponse;
import ru.java.filesharing.web.dto.user.response.UpdateUserResponse;

@Mapper(componentModel = "spring", uses = FileMapper.class)
public interface UserMapper {
    GetUserResponse mapToGetUserResponse(User user);

    UpdateUserResponse mapToUpdateUserResponse(User user);

    CreateUserResponse mapToCreateUserResponse(User user);

    User mapFromUpdateUserRequestToEntity(UpdateUserRequest request);

    User mapFromCreateUserRequestToEntity(CreateUserRequest request);
}
