package com.innowise.secret_santa.service.page_services;

import com.innowise.secret_santa.model.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.innowise.secret_santa.constants.TestConstants.ACCOUNTS_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGEABLE;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO;
import static com.innowise.secret_santa.constants.TestConstants.PAGES_DTO_RESPONSE_ACCOUNT_DTO;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PagesServiceImplTest {

    @InjectMocks
    private PagesServiceImpl<AccountDto> pagesService;

    @Test
    void should_Get_Pageable(){

        assertEquals(PAGEABLE, pagesService.getPage(PAGES_DTO));
    }

    @Test
    void should_Get_PagesDtoResponse_With_AccountDto(){

        assertEquals(PAGES_DTO_RESPONSE_ACCOUNT_DTO, pagesService.getPagesDtoResponse(PAGES_DTO, ACCOUNTS_DTO));
    }

}