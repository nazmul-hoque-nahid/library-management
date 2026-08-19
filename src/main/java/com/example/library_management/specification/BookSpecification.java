package com.example.library_management.specification;

import com.example.library_management.entity.Author;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.Category;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> search(
            String title,
            String isbn,
            String author,
            Long authorId,
            String category,
            Long categoryId,
            String publisherName,
            Long publisherId,
            Boolean available
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Title: Java, java, jav, ava...
            if (title != null && !title.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        )
                );
            }

            // ISBN: exact match
            if (isbn != null && !isbn.isBlank()) {
                predicates.add(
                        cb.equal(root.get("isbn"), isbn)
                );
            }

            // Author name: Martin, martin, artin...
            if (author != null && !author.isBlank()) {

                Join<Book, Author> authorJoin =
                        root.join("authors");

                predicates.add(
                        cb.like(
                                cb.lower(authorJoin.get("name")),
                                "%" + author.toLowerCase() + "%"
                        )
                );
            }

            // Author ID
            if (authorId != null) {

                Join<Book, Author> authorJoin =
                        root.join("authors");

                predicates.add(
                        cb.equal(authorJoin.get("id"), authorId)
                );
            }

            // Category name
            if (category != null && !category.isBlank()) {

                Join<Book, Category> categoryJoin =
                        root.join("categories");

                predicates.add(
                        cb.like(
                                cb.lower(categoryJoin.get("name")),
                                "%" + category.toLowerCase() + "%"
                        )
                );
            }

            // Category ID
            if (categoryId != null) {

                Join<Book, Category> categoryJoin =
                        root.join("categories");

                predicates.add(
                        cb.equal(categoryJoin.get("id"), categoryId)
                );
            }
            if (publisherName != null && !publisherName.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("publisher").get("name")),
                                "%" + publisherName.toLowerCase() + "%"
                        )
                );
            }

            // Publisher ID
            if (publisherId != null) {

                predicates.add(
                        cb.equal(
                                root.get("publisher").get("id"),
                                publisherId
                        )
                );
            }

            // Available books
            if (available != null) {

                if (available) {
                    predicates.add(
                            cb.greaterThan(
                                    root.get("availableCopy"),
                                    0
                            )
                    );
                } else {
                    predicates.add(
                            cb.equal(
                                    root.get("availableCopy"),
                                    0
                            )
                    );
                }
            }

            query.distinct(true);

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}