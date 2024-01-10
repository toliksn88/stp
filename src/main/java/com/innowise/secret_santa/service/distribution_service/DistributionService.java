package com.innowise.secret_santa.service.distribution_service;

import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.DistributionResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;

import java.util.List;

public interface DistributionService {
    List<DistributionResponseDto> getDistributionsForCurrentAccount(Long accountId);

    DistributionResponseDto getDistributionCurrentAccountByNameGame(Long accountId, String nameGame);

    List<DistributionResponseDto> getAllDistributionForOrganizerByNameGame(Long idAccount, String gameName);

    PagesDtoResponse<Object> getAllDistributions(PagesDto pages);
}
