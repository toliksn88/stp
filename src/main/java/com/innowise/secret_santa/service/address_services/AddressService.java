package com.innowise.secret_santa.service.address_services;

import com.innowise.secret_santa.model.dto.AddressDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;

public interface AddressService {

    AddressDto getAddressDtoById(Long id);
    PagesDtoResponse<Object> getAllAccountsAddress(PagesDto pages);
}