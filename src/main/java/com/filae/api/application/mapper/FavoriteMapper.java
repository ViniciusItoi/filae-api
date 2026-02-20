package com.filae.api.application.mapper;

import com.filae.api.application.dto.favorite.FavoriteResponse;
import com.filae.api.domain.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Favorite entity to FavoriteResponse DTO
 */
@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(source = "establishment.id", target = "establishmentId")
    @Mapping(source = "establishment.name", target = "establishmentName")
    @Mapping(source = "establishment.category", target = "category")
    @Mapping(source = "establishment.city", target = "city")
    @Mapping(source = "establishment.rating", target = "rating")
    FavoriteResponse toResponse(Favorite favorite);
}

