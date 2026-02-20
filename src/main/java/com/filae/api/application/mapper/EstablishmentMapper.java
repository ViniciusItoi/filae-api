package com.filae.api.application.mapper;

import com.filae.api.application.dto.establishment.EstablishmentResponse;
import com.filae.api.domain.entity.Establishment;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for Establishment entity to EstablishmentResponse DTO
 */
@Mapper(componentModel = "spring")
public interface EstablishmentMapper {

    EstablishmentResponse toResponse(Establishment establishment);
}

