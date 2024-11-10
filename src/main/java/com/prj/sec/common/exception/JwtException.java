package com.prj.sec.common.exception;

public class JwtException extends RuntimeException {

    private final JwtExceptionType type;

    public JwtException(JwtExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    public JwtExceptionType getType() {
        return type;
    }

}
