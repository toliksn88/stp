package com.innowise.secret_santa.mapper;

import com.innowise.secret_santa.model.dto.response_dto.DistributionResponseDto;
import com.innowise.secret_santa.model.postgres.Distribution;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DistributionMapper {

    DistributionResponseDto toDistributionResponseDto(Distribution distribution);
}
