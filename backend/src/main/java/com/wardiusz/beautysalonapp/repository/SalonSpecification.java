package com.wardiusz.beautysalonapp.repository;

import com.wardiusz.beautysalonapp.dto.salon.SalonFilter;
import com.wardiusz.beautysalonapp.entity.Salon;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SalonSpecification {

    public static Specification<Salon> withFilter(SalonFilter filter) {
        if (filter == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        return Specification
                .where(byDistrict(filter.getDistrict()))
                .and(byServices(filter.getServices()));
    }

    private static Specification<Salon> byDistrict(String district) {
        return (root, query, cb) -> {
            if (district == null || district.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("district"), district);
        };
    }

    private static Specification<Salon> byServices(List<String> services) {
        return (root, query, cb) -> {
            if (services == null || services.isEmpty())
                return cb.conjunction();

            query.distinct(true);

            Join<Salon, String> serviceJoin = root.join("services");
            return serviceJoin.in(services);
        };
    }

}
