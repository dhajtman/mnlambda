package org.example;

import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;

@Singleton
public class EntsoeConfig {

    private final String apiUrl;
    private final String apiUrlToken;
    private final String documentType;
    private final String processType;
    private final String inDomain;
    private final String periodStart;
    private final String periodEnd;
    private final String s3Bucket;
    private final String outputPrefix;
    private final String targetKey;

    public EntsoeConfig(
            @Property(name = "API_URL") String apiUrl,
            @Property(name = "API_URL_TOKEN") String apiUrlToken,
            @Property(name = "DOCUMENT_TYPE") String documentType,
            @Property(name = "PROCESS_TYPE") String processType,
            @Property(name = "IN_DOMAIN") String inDomain,
            @Property(name = "PERIOD_START") String periodStart,
            @Property(name = "PERIOD_END") String periodEnd,
            @Property(name = "S3_BUCKET") String s3Bucket,
            @Property(name = "OUTPUT_PREFIX") String outputPrefix,
            @Property(name = "TARGET_KEY") String targetKey
    ) {
        this.apiUrl = apiUrl;
        this.apiUrlToken = apiUrlToken;
        this.documentType = documentType;
        this.processType = processType;
        this.inDomain = inDomain;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.s3Bucket = s3Bucket;
        this.outputPrefix = outputPrefix;
        this.targetKey = targetKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiUrlToken() {
        return apiUrlToken;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getProcessType() {
        return processType;
    }

    public String getInDomain() {
        return inDomain;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public String getTargetKey() {
        return targetKey;
    }
}