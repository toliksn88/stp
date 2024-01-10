package com.innowise.secret_santa.service.address_services;

import com.innowise.secret_santa.mapper.AddressMapper;
import com.innowise.secret_santa.model.dto.AddressDto;
import com.innowise.secret_santa.repository.AddressRepository;
import com.innowise.secret_santa.service.page_services.PageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.innowise.secret_santa.constants.TestConstants.ACCOUNTS_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ACCOUNT_WITH_PROFILE;
import static com.innowise.secret_santa.constants.TestConstants.ADDRESS;
import static com.innowise.secret_santa.constants.TestConstants.ADDRESSES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ADDRESS_DTO;
import static com.innowise.secret_santa.constants.TestConstants.ID;
import static com.innowise.secret_santa.constants.TestConstants.PAGEABLE;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_ACCOUNT_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_ADDRESS_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_ACCOUNT;
import static com.innowise.secret_santa.constants.TestConstants.PAGE_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;
    @Mock
    private PageService<AddressDto> pageService;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void should_Get_AddressDto_By_Id() {
        given(addressRepository.findById(ID)).willReturn(Optional.of(ADDRESS));
        given(addressMapper.toAddressDto(ADDRESS)).willReturn(ADDRESS_DTO);

        assertEquals(ADDRESS_DTO, addressService.getAddressDtoById(ID));

        then(addressRepository).should(times(1)).findById(ID);
        then(addressMapper).should(times(1)).toAddressDto(ADDRESS);
    }

    @Test
    void should_Get_PagesDtoResponse_With_All_Addresses(){
        given(pageService.getPage(PAGES_DTO)).willReturn(PAGEABLE);
        given(addressRepository.findAll(PAGEABLE)).willReturn(PAGE_ADDRESS);
        given(addressMapper.toAddressDto(ADDRESS)).willReturn(ADDRESS_DTO);
        given(pageService.getPagesDtoResponse(PAGES_DTO, ADDRESSES_DTO)).willReturn(PAGES_DTO_RESPONSE_ADDRESS_DTO
        );

        assertEquals(PAGES_DTO_RESPONSE_ADDRESS_DTO, addressService.getAllAccountsAddress(PAGES_DTO));

        then(pageService).should(times(1)).getPage(PAGES_DTO);
        then(addressRepository).should(times(1)).findAll(PAGEABLE);
        then(addressMapper).should(times(1)).toAddressDto(ADDRESS);
        then(pageService).should(times(1)).getPagesDtoResponse(PAGES_DTO, ADDRESSES_DTO);
    }

}