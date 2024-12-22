package com.mft.benmezunum.mapper;


import com.mft.benmezunum.dto.request.SignupTeacherRQ;
import com.mft.benmezunum.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper( AuthMapper.class );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    Auth map(SignupTeacherRQ dto);

}
