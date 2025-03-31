package gt.electronic.ecommerce.exceptions;

import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.exceptions.error.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author quang huy
 * @created 07/03/2025 - 11:20 PM
 * @project gt-backend
 */
@ControllerAdvice
public class GlobalExceptionsHandler {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  // BindException – This exception is thrown when fatal binding errors occur.
  // MethodArgumentNotValidException – This exception is thrown when an argument
  // annotated with
  // @Valid failed validation:
  @ExceptionHandler({ MethodArgumentNotValidException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected ResponseObject<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpStatus status,
      WebRequest request) {
    // List<String> errors = new ArrayList<String>();
    // for (FieldError error : ex.getBindingResult().getFieldErrors()) {
    // errors.add(error.getField() + ": " + error.getDefaultMessage());
    // }
    // for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
    // errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    // }

    return new ResponseObject<>(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
  }

  // MissingServletRequestPartException – This exception is thrown when the part
  // of a multipart
  // request is not found.
  // MissingServletRequestParameterException – This exception is thrown when the
  // request is missing
  // a parameter
  @ExceptionHandler({ MissingServletRequestParameterException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected ResponseObject<?> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    // String error = ex.getParameterName() + " parameter is missing";
    //
    // ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
    // ex.getLocalizedMessage(), error);
    return new ResponseObject<>(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
  }

  // ConstraintViolationException – This exception reports the result of
  // constraint violations:
  @ExceptionHandler({ ConstraintViolationException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseObject<?> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    // List<String> errors = new ArrayList<String>();
    // for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
    // errors.add(
    // violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() +
    // ": " + violation.getMessage());
    // }
    //
    // ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
    // ex.getLocalizedMessage(), errors);
    return new ResponseObject<>(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
  }

  // TypeMismatchException – This exception is thrown when trying to set bean
  // property with the
  // wrong type.
  // MethodArgumentTypeMismatchException – This exception is thrown when method
  // argument is not the
  // expected type.

  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseObject<?> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
    return new ResponseObject<>(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
  }

  @ExceptionHandler({ NoHandlerFoundException.class })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  protected ResponseObject<?> handleNoHandlerFoundException(NoHandlerFoundException ex,
      HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    // String error = "No handler found for " + ex.getHttpMethod() + " " +
    // ex.getRequestURL();
    //
    // ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
    // ex.getLocalizedMessage(), error);
    return new ResponseObject<>(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
  }

  // The HttpRequestMethodNotSupportedException occurs when we send a requested
  // with an unsupported HTTP method
  @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  protected ResponseObject<?> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    // StringBuilder builder = new StringBuilder();
    // builder.append(ex.getMethod());
    // builder.append(" method is not supported for this request. Supported methods
    // are ");
    // ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
    //
    // ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED,
    // ex.getLocalizedMessage(), builder.toString());
    return new ResponseObject<>(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage());
  }

  // handle HttpMediaTypeNotSupportedException, which occurs when the client sends
  // a request with unsupported media type
  @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ResponseBody
  protected ResponseObject<?> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    // StringBuilder builder = new StringBuilder();
    // builder.append(ex.getContentType());
    // builder.append(" media type is not supported. Supported media types are ");
    // ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
    //
    // ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
    // ex.getLocalizedMessage(),
    // builder.substring(0, builder.length() - 2));
    return new ResponseObject<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  protected ResponseObject<?> handlerResourceNotFoundException(
      RuntimeException exception) {
    return new ResponseObject<>(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(ResourceNotFound.class)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected ResponseObject<?> handlerExceptionInGetRequest(
      RuntimeException ex, WebRequest request) {
    return new ResponseObject<>(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({ ResourceAlreadyExistsException.class, ResourceNotValidException.class,
      ResourceNotSufficientException.class })
  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
  @ResponseBody
  protected ResponseObject<?> handlerImplementedException(
      RuntimeException exception) {
    return new ResponseObject<>(HttpStatus.NOT_IMPLEMENTED, exception.getMessage());
  }

  @ExceptionHandler({ FileException.class,
      InvalidFieldException.class, UserNotPermissionException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  protected ResponseObject<?> handleResourceAlreadyExistsException(
      RuntimeException exception) {
    return new ResponseObject<>(HttpStatus.OK, exception.getMessage());
  }

  @ExceptionHandler({ AccessTokenNotValidException.class })
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  protected ResponseObject<?> handleAccessTokenNotValidException(
      RuntimeException exception) {
    return new ResponseObject<>(HttpStatus.UNAUTHORIZED, exception.getMessage());
  }

  // @ExceptionHandler({MissingServletRequestPartException.class,
  // BindException.class})
  // @ResponseStatus(HttpStatus.OK)
  // @ResponseBody
  // protected ResponseEntity<ResponseObject> handleBadRequestException(
  // RuntimeException exception) {
  // return ResponseEntity.status(HttpStatus.OK)
  // .body(new ResponseObject(HttpStatus.BAD_REQUEST, exception.getMessage()));
  // }
}
