package com.matadesigns.keyprapi;

public class JWTExceptions {
    public static class JWTInvalidStructureException extends Exception {
        public JWTInvalidStructureException(String errMsg) {
            super(errMsg);
        }
    }
    public static class JWTInvalidPayloadException extends Exception {
        public JWTInvalidPayloadException(String errMsg) {
            super(errMsg);
        }
    }

}
