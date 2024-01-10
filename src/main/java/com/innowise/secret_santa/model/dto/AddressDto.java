package com.innowise.secret_santa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto implements Serializable {

    private String country;

    private String city;

    private String street;

    private String numberHouse;

    private String numberApartment;
}
