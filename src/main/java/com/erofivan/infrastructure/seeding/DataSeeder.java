package com.erofivan.infrastructure.seeding;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.domain.cars.Car;
import com.erofivan.domain.cars.CarStaticSpec;
import com.erofivan.domain.cars.CarVisualSpec;
import com.erofivan.domain.cars.bodytypes.SedanBodyType;
import com.erofivan.domain.cars.brands.audi.models.audia4.AudiA4Model;
import com.erofivan.domain.cars.brands.bmw.models.bmw320i.Bmw320iModel;
import com.erofivan.domain.cars.brands.bmw.models.bmw330i.Bmw330iModel;
import com.erofivan.domain.cars.brands.bmw.models.bmwm340i.BmwM340iModel;
import com.erofivan.domain.cars.brands.mercedes.models.mercedese300.MercedesE300Model;
import com.erofivan.domain.cars.colors.BlackColor;
import com.erofivan.domain.cars.colors.BlueColor;
import com.erofivan.domain.cars.colors.GrayColor;
import com.erofivan.domain.cars.colors.WhiteColor;
import com.erofivan.domain.cars.drivetrains.AllWheelDriveType;
import com.erofivan.domain.cars.drivetrains.FrontWheelDriveType;
import com.erofivan.domain.cars.drivetrains.RearWheelDriveType;
import com.erofivan.domain.cars.engines.Engine;
import com.erofivan.domain.cars.engines.EnginePower;
import com.erofivan.domain.cars.engines.EngineVolume;
import com.erofivan.domain.cars.fuels.DieselFuelType;
import com.erofivan.domain.cars.fuels.PetrolFuelType;
import com.erofivan.domain.cars.transmissions.AutomaticTransmissionType;
import com.erofivan.domain.common.Money;
import com.erofivan.domain.common.ids.CarId;
import com.erofivan.domain.common.ids.PartId;
import com.erofivan.domain.common.ids.UserId;
import com.erofivan.domain.parts.Part;
import com.erofivan.domain.parts.PartCompatibility;
import com.erofivan.domain.users.Client;
import com.erofivan.domain.users.Manager;
import com.erofivan.domain.users.SystemAdministrator;
import com.erofivan.domain.users.WarehouseAdministrator;

import java.util.Set;

public final class DataSeeder {
    private DataSeeder() {
    }

    public static SeedData seed(IPersistenceContext context) {
        UserId clientId = UserId.generate();
        context.users().addClient(new Client(clientId, "Demo Client"));

        UserId managerId = UserId.generate();
        context.users().addManager(new Manager(managerId, "Demo Manager"));

        context.users().addWarehouseAdministrator(new WarehouseAdministrator(UserId.generate(), "Warehouse Admin"));
        context.users().addSystemAdministrator(new SystemAdministrator(UserId.generate(), "System Admin"));

        Car firstCar = new Car(
                CarId.generate(),
                new Bmw320iModel(),
                new CarStaticSpec(
                        Engine.of(new PetrolFuelType(), EnginePower.of(184), EngineVolume.of(2.0)),
                        new AutomaticTransmissionType(),
                        new RearWheelDriveType()
                ),
                new CarVisualSpec(new SedanBodyType(), new BlackColor()),
                Money.of(4_100_000L),
                true,
                true
        );

        context.cars().add(firstCar);
        context.cars().add(new Car(
                CarId.generate(),
                new Bmw330iModel(),
                new CarStaticSpec(
                        Engine.of(new PetrolFuelType(), EnginePower.of(258), EngineVolume.of(2.0)),
                        new AutomaticTransmissionType(),
                        new RearWheelDriveType()
                ),
                new CarVisualSpec(new SedanBodyType(), new GrayColor()),
                Money.of(4_800_000L),
                true,
                true
        ));

        context.cars().add(new Car(
                CarId.generate(),
                new BmwM340iModel(),
                new CarStaticSpec(
                        Engine.of(new PetrolFuelType(), EnginePower.of(387), EngineVolume.of(3.0)),
                        new AutomaticTransmissionType(),
                        new AllWheelDriveType()
                ),
                new CarVisualSpec(new SedanBodyType(), new BlueColor()),
                Money.of(6_150_000L),
                true,
                true
        ));

        context.cars().add(new Car(
                CarId.generate(),
                new AudiA4Model(),
                new CarStaticSpec(
                        Engine.of(new DieselFuelType(), EnginePower.of(190), EngineVolume.of(2.0)),
                        new AutomaticTransmissionType(),
                        new FrontWheelDriveType()
                ),
                new CarVisualSpec(new SedanBodyType(), new WhiteColor()),
                Money.of(3_950_000L),
                true,
                true
        ));

        context.cars().add(new Car(
                CarId.generate(),
                new MercedesE300Model(),
                new CarStaticSpec(
                        Engine.of(new PetrolFuelType(), EnginePower.of(258), EngineVolume.of(2.0)),
                        new AutomaticTransmissionType(),
                        new RearWheelDriveType()
                ),
                new CarVisualSpec(new SedanBodyType(), new BlackColor()),
                Money.of(5_100_000L),
                true,
                true
        ));

        context.parts().add(new Part(
                PartId.generate(),
                "Brake Pads",
                "Front axle performance brake pads",
                Money.of(28_000L),
                PartCompatibility.of(Set.of("BMW-320I", "BMW-330I", "BMW-M340I"))
        ));

        context.parts().add(new Part(
                PartId.generate(),
                "Cabin Air Filter",
                "Activated carbon filter",
                Money.of(9_500L),
                PartCompatibility.of(Set.of("BMW-320I", "AUDI-A4", "MERCEDES-E300"))
        ));

        return new SeedData(clientId, firstCar.getId());
    }

    public record SeedData(UserId demoClientId, CarId demoCarId) {
    }
}
