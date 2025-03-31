// package gt.electronic.ecommerce.exceptions;
//
// import gt.electronic.ecommerce.dto.response.ResponseObject;
// import gt.electronic.ecommerce.exceptions.error.ApiError;
// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.MalformedJwtException;
// import io.jsonwebtoken.UnsupportedJwtException;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.AccessDeniedException;
// import
// org.springframework.security.authentication.InsufficientAuthenticationException;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.validation.FieldError;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.HttpMediaTypeNotSupportedException;
// import org.springframework.web.HttpRequestMethodNotSupportedException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.MissingServletRequestParameterException;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.context.request.WebRequest;
// import
// org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
// import org.springframework.web.servlet.NoHandlerFoundException;
// import
// org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
// import javax.validation.ConstraintViolation;
// import javax.validation.ConstraintViolationException;
// import java.util.ArrayList;
// import java.util.List;
//
/// **
// * @author quang huy
// * @created 11/09/2025 - 2:09 PM
// * @project gt-backend
// */
// @ControllerAdvice
// public class CustomRestExceptionHandler extends
// ResponseEntityExceptionHandler {
// private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
//
// // BindException – This exception is thrown when fatal binding errors occur.
// // MethodArgumentNotValidException – This exception is thrown when an
// argument annotated with
// // @Valid failed validation:
// @Override protected ResponseEntity<Object>
// handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
// HttpHeaders headers, HttpStatus status,
// WebRequest request) {
// List<String> errors = new ArrayList<String>();
// for (FieldError error : ex.getBindingResult().getFieldErrors()) {
// errors.add(error.getField() + ": " + error.getDefaultMessage());
// }
// for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
// errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
// }
//
// ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
// ex.getLocalizedMessage(), errors);
// return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(),
// request);
// }
//
// // MissingServletRequestPartException – This exception is thrown when the
// part of a multipart
// // request is not found.
// // MissingServletRequestParameterException – This exception is thrown when
// the request is missing
// // a parameter
// @Override protected ResponseEntity<Object>
// handleMissingServletRequestParameter(
// MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus
// status, WebRequest request) {
// String error = ex.getParameterName() + " parameter is missing";
//
// ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
// ex.getLocalizedMessage(), error);
// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
// }
//
// // ConstraintViolationException – This exception reports the result of
// constraint violations:
// @ExceptionHandler({ConstraintViolationException.class}) public
// ResponseEntity<Object> handleConstraintViolation(
// ConstraintViolationException ex, WebRequest request) {
// List<String> errors = new ArrayList<String>();
// for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
// errors.add(
// violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() +
// ": " + violation.getMessage());
// }
//
// ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
// ex.getLocalizedMessage(), errors);
// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
// }
//
// // TypeMismatchException – This exception is thrown when trying to set bean
// property with the
// // wrong type.
// // MethodArgumentTypeMismatchException – This exception is thrown when method
// argument is not the
// // expected type.
//
// @ExceptionHandler({MethodArgumentTypeMismatchException.class}) public
// ResponseEntity<Object> handleMethodArgumentTypeMismatch(
// MethodArgumentTypeMismatchException ex, WebRequest request) {
// String error = ex.getName() + " should be of type " +
// ex.getRequiredType().getName();
//
// ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
// ex.getLocalizedMessage(), error);
// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
// }
//
// @Override protected ResponseEntity<Object>
// handleNoHandlerFoundException(NoHandlerFoundException ex,
// HttpHeaders headers, HttpStatus status,
// WebRequest request) {
// String error = "No handler found for " + ex.getHttpMethod() + " " +
// ex.getRequestURL();
//
// ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
// ex.getLocalizedMessage(), error);
// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
// }
//
// // The HttpRequestMethodNotSupportedException occurs when we send a requested
// with an unsupported HTTP method
// @Override protected ResponseEntity<Object>
// handleHttpRequestMethodNotSupported(
// HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus
// status, WebRequest request) {
// StringBuilder builder = new StringBuilder();
// builder.append(ex.getMethod());
// builder.append(" method is not supported for this request. Supported methods
// are ");
// ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
//
// ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED,
// ex.getLocalizedMessage(), builder.toString());
// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
// }
//
// // handle HttpMediaTypeNotSupportedException, which occurs when the client
// sends a request with unsupported media type
// @Override protected ResponseEntity<Object>
// handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
// HttpHeaders headers, HttpStatus status,
// WebRequest request) {
// StringBuilder builder = new StringBuilder();
// builder.append(ex.getContentType());
// builder.append(" media type is not supported. Supported media types are ");
// ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
//
// ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
// ex.getLocalizedMessage(),
// builder.substring(0, builder.length() - 2));
// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
// }
//
// @ExceptionHandler(ResourceNotFoundException.class) protected
// ResponseEntity<Object> handlerResourceNotFoundException(
// RuntimeException ex, WebRequest request) {
// ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
// ex.getLocalizedMessage(), "error occurred");
// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
// }
//
// @ExceptionHandler(ResourceNotFound.class) @ResponseStatus(HttpStatus.OK)
// @ResponseBody protected ResponseObject<?> handlerExceptionInGetRequest(
// RuntimeException ex, WebRequest request) {
// return new ResponseObject<>(HttpStatus.NOT_FOUND, ex.getMessage(), null);
// }
//
//// @ExceptionHandler({AccessDeniedException.class})
// @ResponseStatus(HttpStatus.OK) @ResponseBody protected ResponseObject<?>
// handleUNAUTHORIZEDException(
//// Exception ex, WebRequest request) {
//// return new ResponseObject<>(HttpStatus.UNAUTHORIZED,
// ex.getLocalizedMessage(), null);
//// }
////
//// @ExceptionHandler({AuthenticationException.class})
//// @ResponseStatus(HttpStatus.UNAUTHORIZED)
//// @ResponseBody
//// public ResponseObject<?> handleAuthenticationException(Exception ex) {
//// return new ResponseObject<>(HttpStatus.UNAUTHORIZED,
// ex.getLocalizedMessage(), null);
//// }
//
// @ExceptionHandler({FileException.class, InvalidFieldException.class,
// UserNotPermissionException.class}) @ResponseStatus(HttpStatus.BAD_REQUEST)
// @ResponseBody protected ResponseObject<?>
// handleResourceAlreadyExistsException(
// RuntimeException exception) {
// return new ResponseObject<>(HttpStatus.OK, exception.getMessage(), null);
// }
//
// // a fallback handler — a catch-all type of logic that deals with all other
// exceptions that don't have specific handlers
//// @ExceptionHandler({Exception.class}) public ResponseEntity<Object>
// handleAll(Exception ex, WebRequest request) {
//// ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
// ex.getLocalizedMessage(), "error occurred");
//// return new ResponseEntity<Object>(apiError, new HttpHeaders(),
// apiError.getStatus());
//// }
//
// }
