package com.erofivan.application.contracts.services;

import com.erofivan.presentation.dtos.responses.PartResponse;

import java.util.List;

public interface PartService {
    List<PartResponse> listParts();
}
