package net.thumbtack.school.concert.server;

import net.thumbtack.school.concert.exception.ErrorCode;

public class ServerException extends Exception{
    private ErrorCode error;
    private String message;

    public ServerException(ErrorCode error) {
        super();
        this.error = error;
        this.message = error.getErrorString();
    }

    public ErrorCode getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
