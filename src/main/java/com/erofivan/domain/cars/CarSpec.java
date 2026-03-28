package com.erofivan.domain.cars;

import com.erofivan.domain.cars.drivetrains.DrivetrainType;
import com.erofivan.domain.cars.engines.Engine;
import com.erofivan.domain.cars.transmissions.TransmissionType;

public record CarSpec(Engine engine, TransmissionType transmissionType,
                      DrivetrainType drivetrainType) {
}
