package org.example;

import io.micronaut.context.annotation.Property;

public class EntsoeConfig {

    @Property(name = "API_URL")
    private String API_URL;

    @Property(name = "API_URL_TOKEN")
    private String API_URL_TOKEN;

    @Property(name = "DOCUMENT_TYPE")
    private String DOCUMENT_TYPE;

    @Property(name = "PROCESS_TYPE")
    private String PROCESS_TYPE;

    @Property(name = "IN_DOMAIN")
    private String IN_DOMAIN;

    @Property(name = "PERIOD_START")
    private String PERIOD_START;

    @Property(name = "PERIOD_END")
    private String PERIOD_END;

    @Property(name = "S3_BUCKET")
    private String S3_BUCKET;

    @Property(name = "OUTPUT_PREFIX")
    private String OUTPUT_PREFIX;

    @Property(name = "TARGET_KEY")
    private String TARGET_KEY;

    public String getAPI_URL() {
        return API_URL;
    }

    public void setAPI_URL(String API_URL) {
        this.API_URL = API_URL;
    }

    public String getAPI_URL_TOKEN() {
        return API_URL_TOKEN;
    }

    public void setAPI_URL_TOKEN(String API_URL_TOKEN) {
        this.API_URL_TOKEN = API_URL_TOKEN;
    }

    public String getDOCUMENT_TYPE() {
        return DOCUMENT_TYPE;
    }

    public void setDOCUMENT_TYPE(String DOCUMENT_TYPE) {
        this.DOCUMENT_TYPE = DOCUMENT_TYPE;
    }

    public String getPROCESS_TYPE() {
        return PROCESS_TYPE;
    }

    public void setPROCESS_TYPE(String PROCESS_TYPE) {
        this.PROCESS_TYPE = PROCESS_TYPE;
    }

    public String getIN_DOMAIN() {
        return IN_DOMAIN;
    }

    public void setIN_DOMAIN(String IN_DOMAIN) {
        this.IN_DOMAIN = IN_DOMAIN;
    }

    public String getPERIOD_START() {
        return PERIOD_START;
    }

    public void setPERIOD_START(String PERIOD_START) {
        this.PERIOD_START = PERIOD_START;
    }

    public String getPERIOD_END() {
        return PERIOD_END;
    }

    public void setPERIOD_END(String PERIOD_END) {
        this.PERIOD_END = PERIOD_END;
    }

    public String getS3_BUCKET() {
        return S3_BUCKET;
    }

    public void setS3_BUCKET(String s3_BUCKET) {
        this.S3_BUCKET = s3_BUCKET;
    }

    public String getOUTPUT_PREFIX() {
        return OUTPUT_PREFIX;
    }

    public void setOUTPUT_PREFIX(String OUTPUT_PREFIX) {
        this.OUTPUT_PREFIX = OUTPUT_PREFIX;
    }

    public String getTARGET_KEY() {
        return TARGET_KEY;
    }

    public void setTARGET_KEY(String TARGET_KEY) {
        this.TARGET_KEY = TARGET_KEY;
    }
}
