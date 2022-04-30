package com.epam.esm.dto.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * TagDtoMapper
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Component
@Qualifier("tagDtoMapper")
public class TagMapper extends Mapper<Tag, TagDto> {

    protected TagMapper(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public Tag toEntity(TagDto dto) {
        return modelMapper.map(dto, Tag.class);
    }

    @Override
    public TagDto toDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }
}
