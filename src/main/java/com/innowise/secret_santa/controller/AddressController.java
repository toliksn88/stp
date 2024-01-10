package com.innowise.secret_santa.controller;

import com.innowise.secret_santa.constants_message.Constants;
import com.innowise.secret_santa.model.dto.AddressDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.service.address_services.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/accounts/profile/addresses")
@Api("Address Rest Controller")
@Validated
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{addressId}")
    @ApiOperation("Get address by id")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable("addressId")
                                                     @Positive(message = Constants.NOT_NEGATIVE_ID)
                                                     Long addressId) {

        AddressDto addressDtoById = addressService.getAddressDtoById(addressId);
        return new ResponseEntity<>(addressDtoById, HttpStatus.OK);
    }

    @GetMapping("/all")
    @ApiOperation("Get all addresses")
    @PreAuthorize("hasPermission('ROLE_ADMIN', authentication.principal.authorities)")
    public ResponseEntity<PagesDtoResponse<Object>> getAllAddresses
            (@RequestParam(defaultValue = "5") int size,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(required = false, defaultValue = "email") String sort) {

        PagesDtoResponse<Object> allAddresses = addressService.getAllAccountsAddress(
                PagesDto
                        .builder()
                        .sort(sort)
                        .size(size)
                        .page(page)
                        .build());

        return new ResponseEntity<>(allAddresses, HttpStatus.OK);
    }
}