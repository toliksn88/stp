package com.innowise.secret_santa.service.address_services;

import com.innowise.secret_santa.exception.NoDataFoundException;
import com.innowise.secret_santa.mapper.AddressMapper;
import com.innowise.secret_santa.model.dto.AddressDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.model.postgres.Address;
import com.innowise.secret_santa.repository.AddressRepository;
import com.innowise.secret_santa.service.page_services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService,
        AddressProfileService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final PageService<AddressDto> pageService;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository,
                              AddressMapper addressMapper,
                              PageService<AddressDto> pageService) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.pageService = pageService;
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getAddressDtoById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(addressRepository::findById)
                .map(addressMapper::toAddressDto)
                .orElseThrow(() -> new NoDataFoundException("Address by id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PagesDtoResponse<Object> getAllAccountsAddress(PagesDto pages) {
        Page<AddressDto> listAddress = addressRepository.findAll(pageService.getPage(pages))
                .map(addressMapper::toAddressDto);
        if (listAddress.isEmpty()) {
            throw new NoDataFoundException("Addresses not found");
        }
        return pageService.getPagesDtoResponse(pages, listAddress.getContent());
    }

    @Override
    @Transactional
    public Address changeAddressData(Address oldAddress, AddressDto newAddress) {

        String country = newAddress.getCountry();
        String city = newAddress.getCity();
        String street = newAddress.getStreet();
        String numberHouse = newAddress.getNumberHouse();
        String numberApartment = newAddress.getNumberApartment();

        if (!country.isBlank() && !country.equals(oldAddress.getCountry())) {
            oldAddress.setCountry(country);
        }
        if (!city.isBlank() && !city.equals(oldAddress.getCity())) {
            oldAddress.setCity(city);
        }
        if (!street.isBlank() && !street.equals(oldAddress.getStreet())) {
            oldAddress.setStreet(street);
        }
        if (!numberHouse.isBlank() && !numberHouse.equals(oldAddress.getNumberHouse())) {
            oldAddress.setNumberHouse(numberHouse);
        }
        if (!numberApartment.isBlank() && !numberApartment.equals(oldAddress.getNumberApartment())) {
            oldAddress.setNumberApartment(numberApartment);
        }
        return oldAddress;
    }
}