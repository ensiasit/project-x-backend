package com.ensiasit.projectx.utils;

import com.ensiasit.projectx.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

@Component
public class Helpers {
    public <T> T extractById(long id, JpaRepository<T, Long> repository) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id=%d not found.", id)));
    }

    public <T> T extract(Supplier<Optional<T>> supplier) {
        return supplier.get()
                .orElseThrow(() -> new NotFoundException("Item not found."));
    }
}
