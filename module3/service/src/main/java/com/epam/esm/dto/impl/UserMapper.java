package com.epam.esm.dto.impl;

import com.epam.esm.domain.User;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * UserDtoMapper
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Component
@Qualifier("userDtoMapper")
public class UserMapper extends Mapper<User, UserDto> {

    protected UserMapper(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public User toEntity(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    @Override
    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
