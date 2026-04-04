package Projeto_Votos.main.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;

public class NotFoundExceptionHandler extends RuntimeException {
    public NotFoundExceptionHandler(String message) {
        super(message);
    }
}
