/*

 */

package org.gmu.chess.components;

import org.gmu.chess.exceptions.UserAlreadyExistException;
import org.gmu.chess.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    private static final ResponseEntity<Object> NOT_FOUND = ResponseEntity.notFound().build();
    private static final ResponseEntity<Object> CONFLICT = ResponseEntity.status(HttpStatus.CONFLICT).build();

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity handleUserAlreadyExistException() {
        return CONFLICT;
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException() {
        return NOT_FOUND;
    }
}
