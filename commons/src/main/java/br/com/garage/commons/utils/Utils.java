package br.com.garage.commons.utils;

import br.com.garage.commons.exceptions.NotFoundException;

import java.util.Optional;

public class Utils {

    public static final <T> T requireNotEmpty(Optional<T> optional) {
        return optional.orElseThrow(() -> new NotFoundException("NOT FOUND"));
    }
}
