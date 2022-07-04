package com.securitish.safebox;

public class ApplicationTestConstants {
    public static final String ROOF_ENDPOINT = "http://localhost:8080";
    public static final String ENDPOINT_BETA = "/api/beta";
    public static final String ENDPOINT_V1 = "/api/v1";

    public static final String SAFEBOX_ENDPOINT_BETA = ROOF_ENDPOINT + ENDPOINT_BETA + "/safebox";
    public static final String EXIST_SAFEBOX_ENDPOINT_BETA = SAFEBOX_ENDPOINT_BETA + "/1/items";
    public static final String NOT_EXIST_SAFEBOX_ENDPOINT_BETA = SAFEBOX_ENDPOINT_BETA + "/99/items";

    public static final String SAFEBOX_ENDPOINT_V1 = ROOF_ENDPOINT + ENDPOINT_V1 + "/safebox";
    public static final String EXIST_SAFEBOX_ENDPOINT_V1 = SAFEBOX_ENDPOINT_V1 + "/1/items";
    public static final String EXIST_SAFEBOX_ENDPOINT_V1_2 = SAFEBOX_ENDPOINT_V1 + "/2/items";
    public static final String NOT_EXIST_SAFEBOX_ENDPOINT_V1 = SAFEBOX_ENDPOINT_V1 + "/99/items";
    public static final String OPEN_EXIST_SAFEBOX_ENDPOINT_V1 = SAFEBOX_ENDPOINT_V1 + "/1/open";
    public static final String OPEN_EXIST_SAFEBOX_ENDPOINT_V1_2 = SAFEBOX_ENDPOINT_V1 + "/2/open";
    public static final String OPEN_NOT_EXIST_SAFEBOX_ENDPOINT_V1 = SAFEBOX_ENDPOINT_V1 + "/99/open";
}
