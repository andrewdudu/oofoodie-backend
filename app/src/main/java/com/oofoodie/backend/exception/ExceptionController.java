package com.oofoodie.backend.exception;

import com.blibli.oss.command.exception.CommandValidationException;
import com.oofoodie.backend.models.response.ResponseHelper;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<?> handleException(ServerWebInputException ex){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<?> handleException(DecodingException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleException(ExpiredJwtException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleException(IllegalArgumentException ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleException(NotFoundException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleException(BadRequestException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(CommandValidationException.class)
    public ResponseEntity<?> handleException(CommandValidationException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationFailException.class)
    public ResponseEntity<?> handleException(AuthenticationFailException ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        log(ex);

        return ResponseEntity.status(httpStatus).body(ResponseHelper.error(httpStatus, ex.getMessage()));
    }

    private void log (Exception ex){
        Logger logger = LoggerFactory.getLogger(ex.getClass());
        logger.error(ex.getMessage());
    }
}
