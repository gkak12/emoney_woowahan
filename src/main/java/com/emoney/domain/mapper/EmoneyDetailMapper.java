package com.emoney.domain.mapper;

import com.emoney.domain.dto.info.InfoEmoneyDetailDto;
import com.emoney.domain.dto.response.ResponseEmoneyDetailDto;
import com.emoney.domain.entity.EmoneyDetail;
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
public interface EmoneyDetailMapper {

    @Mapping(source = "expirationDateTime", target = "expirationDateTime", qualifiedByName = "setLocalDateTimeToString")
    @Mapping(source = "creationDateTime", target = "creationDateTime", qualifiedByName = "setLocalDateTimeToString")
    ResponseEmoneyDetailDto toDto(EmoneyDetail emoneyDetail);

    @Mapping(source = "expirationDateTime", target = "expirationDateTime", qualifiedByName = "setLocalDateTimeToString")
    ResponseEmoneyDetailDto toDto(InfoEmoneyDetailDto emoneyDetail);

    @Named("setLocalDateTimeToString")
    default String setLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
