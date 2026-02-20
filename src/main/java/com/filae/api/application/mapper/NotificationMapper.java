package com.filae.api.application.mapper;

import com.filae.api.application.dto.notification.NotificationResponse;
import com.filae.api.domain.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Notification entity to NotificationResponse DTO
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "type", expression = "java(notification.getType().toString())")
    @Mapping(target = "queueId", expression = "java(notification.getTicket() != null ? notification.getTicket().getId() : null)")
    NotificationResponse toResponse(Notification notification);
}

