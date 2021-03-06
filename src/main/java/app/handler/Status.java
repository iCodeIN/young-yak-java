package app.handler;

/**
 * This enum represents the status of an IHandlerResponse.<br>
 * The status codes are based on the HTTP status codes.
 */
public enum Status {

    STATUS_200_OK,
    STATUS_201_CREATED,
    STATUS_202_ACCEPTED,
    STATUS_203_NON_AUTHORATIVE_INFORMATION,
    STATUS_204_NO_CONTENT,

    STATUS_303_SEE_OTHER,

    STATUS_400_BAD_REQUEST,
    STATUS_401_UNAUTHORIZED,
    STATUS_403_FORBIDDEN,
    STATUS_404_NOT_FOUND

}
