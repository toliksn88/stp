package com.innowise.secret_santa.service.page_services;

import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PageService<T> {

    Pageable getPage(PagesDto pages);

    PagesDtoResponse<Object> getPagesDtoResponse(PagesDto pagesDto, List<T> all);
}
