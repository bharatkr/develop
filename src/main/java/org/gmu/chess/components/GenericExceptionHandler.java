/*

 */

package org.gmu.chess.components;

import org.gmu.chess.exceptions.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler {

    private static final ResponseEntity<Object> NOT_IMPLEMENTED = ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity handleUnsupportedOperationException() {
        return NOT_IMPLEMENTED;
    }
}
