package com.filae.api.application.mapper;

import com.filae.api.application.dto.queue.QueueResponse;
import com.filae.api.domain.entity.Queue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Queue entity to QueueResponse DTO
 */
@Mapper(componentModel = "spring")
public interface QueueMapper {

    @Mapping(source = "establishment.id", target = "establishmentId")
    @Mapping(source = "establishment.name", target = "establishmentName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(target = "status", expression = "java(queue.getStatus().toString())")
    QueueResponse toResponse(Queue queue);
}

