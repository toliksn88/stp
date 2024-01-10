package com.innowise.secret_santa.service.address_services;

import com.innowise.secret_santa.model.dto.AddressDto;
import com.innowise.secret_santa.model.postgres.Address;

public interface AddressProfileService {
    Address changeAddressData(Address oldAddress, AddressDto newAddress);
}
