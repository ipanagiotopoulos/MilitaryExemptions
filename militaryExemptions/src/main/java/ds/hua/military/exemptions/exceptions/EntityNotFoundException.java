package ds.hua.military.exemptions.exceptions;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;


import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(EntityNotFoundException.generateMessage(message));
    }

    private static String generateMessage(String entity) {
        return StringUtils.capitalize(entity);
    }


}

