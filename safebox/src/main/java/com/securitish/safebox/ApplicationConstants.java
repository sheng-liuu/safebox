package com.securitish.safebox;

public class ApplicationConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "Bearer ";

    // error message
    public static final String PASSWORD_UNSAFE = "Password is not insured enough";
    public static final String SAFEBOX_ALREADY_EXISTS = "Safebox already exists";
    public static final String SAFEBOX_NOT_EXISTS = "Requested safebox does not exist or the name and password you are using do not match the id you want to access.";
    public static final String WRONG_COMBINATION_OF_ID_AND_PASSWORD = "Wrong combination between safebox id and password.";
    public static final String SAFEBOX_LOCKED = "Requested safebox is locked.";
    public static final String TOKEN_VOID = "Token void.";
    public static final String TOKEN_EXPIRED = "Token expired.";
}
