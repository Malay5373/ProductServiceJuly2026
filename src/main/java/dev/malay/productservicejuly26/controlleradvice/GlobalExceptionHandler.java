package dev.malay.productservicejuly26.controlleradvice;

import dev.malay.productservicejuly26.dtos.ExceptionDto;
import dev.malay.productservicejuly26.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ExceptionDto> handleArithmaticException(){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Arithmetic exception");
        exceptionDto.setSolution("try to solve it again");
        ResponseEntity<ExceptionDto> response=new ResponseEntity<>(
                exceptionDto,
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<ExceptionDto> handleArrayIndexOutOfBoundException(){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Array index out of bounds");
        exceptionDto.setSolution("Be careful about index");
        ResponseEntity<ExceptionDto> response=new ResponseEntity<>(
                exceptionDto,
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDto> handleNullPointerException(){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("NullPointer exception");
        exceptionDto.setSolution("Please check the exception");
        ResponseEntity<ExceptionDto> response=new ResponseEntity<>(
                exceptionDto,
                HttpStatus.BAD_REQUEST
        );
        return response;
    }
    /*
    -- or we can do it in this way also without call ExceptionDto
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(){
        ResponseEntity<String> response=new ResponseEntity<>(
                "null pointer exception has Happend",
                HttpStatus.BAD_REQUEST
        );
        return response;
    }
     */

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleProductNotFoundException(){
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Product not found");
        exceptionDto.setSolution("Please provide a valid id");
        ResponseEntity<ExceptionDto> response=new ResponseEntity<>(
                exceptionDto,
                HttpStatus.BAD_REQUEST
        );
        return response;
    }
}
