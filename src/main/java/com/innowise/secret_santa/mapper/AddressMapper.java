package com.innowise.secret_santa.mapper;

import com.innowise.secret_santa.model.dto.AddressDto;
import com.innowise.secret_santa.model.postgres.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toAddressDto(Address address);
}
