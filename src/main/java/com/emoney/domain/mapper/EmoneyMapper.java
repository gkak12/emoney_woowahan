package com.emoney.domain.mapper;

import com.emoney.domain.dto.request.RequestEmoneySaveDto;
import com.emoney.domain.dto.response.ResponseEmoneyDto;
import com.emoney.domain.entity.Emoney;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public interface EmoneyMapper {

    Emoney toEntity(RequestEmoneySaveDto emoneySaveDto);

    @Mapping(source = "expirationDateTime", target = "expirationDateTime", qualifiedByName = "setLocalDateTimeToString")
    @Mapping(source = "creationDateTime", target = "creationDateTime", qualifiedByName = "setLocalDateTimeToString")
    ResponseEmoneyDto toDto(Emoney emoney);

    @Named("setLocalDateTimeToString")
    default String setLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
