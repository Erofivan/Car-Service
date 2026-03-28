package com.erofivan.infrastructure.persistence.jpa.specifications;

import com.erofivan.infrastructure.persistence.jpa.model.CarJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.ModelComponentOptionJpaEntity;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public final class CarJpaSpecifications {
    private CarJpaSpecifications() {
    }

    public static Specification<CarJpaEntity> notRemoved() {
        return (root, query, cb) -> cb.isFalse(root.get("removed"));
    }

    public static Specification<CarJpaEntity> hasBrandCode(String brandCode) {
        return (root, query, cb) -> {
            if (brandCode == null || brandCode.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("model").get("brand").get("code"), brandCode);
        };
    }

    public static Specification<CarJpaEntity> hasModelCode(String modelCode) {
        return (root, query, cb) -> {
            if (modelCode == null || modelCode.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("model").get("code"), modelCode);
        };
    }

    public static Specification<CarJpaEntity> hasComponentName(String componentName) {
        return (root, query, cb) -> {
            if (componentName == null || componentName.isBlank()) {
                return cb.conjunction();
            }

            Subquery<java.util.UUID> subquery = query.subquery(java.util.UUID.class);
            var linkRoot = subquery.from(ModelComponentOptionJpaEntity.class);
            var optionJoin = linkRoot.join("componentOption");
            var modelJoin = linkRoot.join("model");

            subquery.select(modelJoin.get("id"))
                .where(cb.equal(optionJoin.get("name"), componentName));

            return root.get("model").get("id").in(subquery);
        };
    }

    public static Specification<CarJpaEntity> byFilters(String brandCode, String modelCode, String componentName) {
        return Specification.where(notRemoved())
            .and(hasBrandCode(brandCode))
            .and(hasModelCode(modelCode))
            .and(hasComponentName(componentName));
    }
}
