package com.erofivan.application.contracts.parts;

import com.erofivan.application.contracts.parts.dtos.PartDto;

import java.util.List;

public interface PartServiceContract {
    List<PartDto> listParts();
}
