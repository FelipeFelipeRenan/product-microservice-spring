package felipe.util.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundException(ServerHttpRequest request,
            NotFoundException ex) {

        return createHttpErrorInfo(NOT_FOUND, request, ex);

    }

    

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(ServerHttpRequest request,
            invalidInputException ex) {

        return createHttpErrorInfo(UNPPROCESSABLE_ENTITY, request, ex);

    }

    private HttpResponseInfo createHttpErrorInfo(
        HttpStatus httpStatus, ServerHttpRequest request, Exception ex
    ){
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        return new HttpErrorInfo(httpStatus, path, message);
    }
}