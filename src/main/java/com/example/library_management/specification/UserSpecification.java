package com.example.library_management.specification;

import com.example.library_management.entity.User;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class UserSpecification {
    public static Specification<User> search(
            String name,
            String email,
            String membershipNumber,
            User.Role role,
            User.Status status
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (email != null && !email.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("email")),
                                "%" + email.toLowerCase() + "%"
                        )
                );
            }

            if (membershipNumber != null && !membershipNumber.isBlank()) {
                predicates.add(
                        cb.equal(
                                root.get("membershipNumber"),
                                membershipNumber
                        )
                );
            }

            if (role != null) {
                predicates.add(
                        cb.equal(root.get("role"), role)
                );
            }

            if (status != null) {
                predicates.add(
                        cb.equal(root.get("status"), status)
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
