package com.erofivan.infrastructure.persistence.jpa.specifications;

import com.erofivan.domain.models.CarEntity;
import com.erofivan.domain.models.ModelComponentOptionEntity;
import jakarta.persistence.criteria.Subquery;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

public final class CarJpaSpecifications {
    private CarJpaSpecifications() {
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> notRemoved() {
        return (root, query, cb) -> cb.isFalse(root.get("removed"));
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasBrandCode(String brandCode) {
        return (root, query, cb) -> {
            if (brandCode == null || brandCode.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("model").get("brand").get("code"), brandCode);
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasModelCode(String modelCode) {
        return (root, query, cb) -> {
            if (modelCode == null || modelCode.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("model").get("code"), modelCode);
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasBodyType(String bodyType) {
        return (root, query, cb) -> {
            if (bodyType == null || bodyType.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("bodyType"), bodyType);
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasFuelType(String fuelType) {
        return (root, query, cb) -> {
            if (fuelType == null || fuelType.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("fuelType"), fuelType);
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasTransmission(String transmission) {
        return (root, query, cb) -> {
            if (transmission == null || transmission.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("transmission"), transmission);
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasDrivetrain(String drivetrain) {
        return (root, query, cb) -> {
            if (drivetrain == null || drivetrain.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("drivetrain"), drivetrain);
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasColor(String color) {
        return (root, query, cb) -> {
            if (color == null || color.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("color"), color);
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasPriceBetween(Long minPrice, Long maxPrice) {
        return (root, query, cb) -> {
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if (maxPrice != null) {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return cb.conjunction();
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasPowerHpBetween(Integer minPower, Integer maxPower) {
        return (root, query, cb) -> {
            if (minPower != null && maxPower != null) {
                return cb.between(root.get("powerHp"), minPower, maxPower);
            }
            if (minPower != null) {
                return cb.greaterThanOrEqualTo(root.get("powerHp"), minPower);
            }
            if (maxPower != null) {
                return cb.lessThanOrEqualTo(root.get("powerHp"), maxPower);
            }
            return cb.conjunction();
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasEngineLitresBetween(Double minEngine, Double maxEngine) {
        return (root, query, cb) -> {
            if (minEngine != null && maxEngine != null) {
                return cb.between(root.get("engineLitres"), minEngine, maxEngine);
            }
            if (minEngine != null) {
                return cb.greaterThanOrEqualTo(root.get("engineLitres"), minEngine);
            }
            if (maxEngine != null) {
                return cb.lessThanOrEqualTo(root.get("engineLitres"), maxEngine);
            }
            return cb.conjunction();
        };
    }

    @Contract(pure = true)
    public static @NonNull Specification<CarEntity> hasComponentName(String componentName) {
        return (root, query, cb) -> {
            if (componentName == null || componentName.isBlank()) {
                return cb.conjunction();
            }

            Subquery<java.util.UUID> subquery = query.subquery(java.util.UUID.class);
            var linkRoot = subquery.from(ModelComponentOptionEntity.class);
            var optionJoin = linkRoot.join("componentOption");
            var modelJoin = linkRoot.join("model");

            subquery.select(modelJoin.get("id"))
                .where(cb.equal(optionJoin.get("name"), componentName));

            return root.get("model").get("id").in(subquery);
        };
    }

    public static @NonNull Specification<CarEntity>
    byFilters(
        String brandCode,
        String modelCode,
        String bodyType,
        String fuelType,
        String transmission,
        String drivetrain,
        String color,
        Long minPrice,
        Long maxPrice,
        Integer minPower,
        Integer maxPower,
        Double minEngine,
        Double maxEngine,
        String componentName
    ) {
        return Specification.where(notRemoved())
            .and(hasBrandCode(brandCode))
            .and(hasModelCode(modelCode))
            .and(hasBodyType(bodyType))
            .and(hasFuelType(fuelType))
            .and(hasTransmission(transmission))
            .and(hasDrivetrain(drivetrain))
            .and(hasColor(color))
            .and(hasPriceBetween(minPrice, maxPrice))
            .and(hasPowerHpBetween(minPower, maxPower))
            .and(hasEngineLitresBetween(minEngine, maxEngine))
            .and(hasComponentName(componentName));
    }
}
