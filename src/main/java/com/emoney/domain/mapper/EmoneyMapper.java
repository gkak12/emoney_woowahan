package com.emoney.domain.mapper;

import com.emoney.domain.dto.request.RequestEmoneySaveDto;
import com.emoney.domain.entity.Emoney;
import org.mapstruct.*;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public interface EmoneyMapper {

    Emoney toEntity(RequestEmoneySaveDto emoneySaveDto);
}
