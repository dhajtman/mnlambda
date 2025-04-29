package org.example;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("entsoe")
public class EntsoeConfig {
    private String apiUrlTemplate;
    private String apiUrlToken;
    private String documentType;
    private String processType;
    private String inDomain;
    private String periodStart;
    private String periodEnd;
    private String bucketName;
    private String outputPrefix;
    private String targetKey;

    public String getApiUrlTemplate() {
        return apiUrlTemplate;
    }

    public void setApiUrlTemplate(String apiUrlTemplate) {
        this.apiUrlTemplate = apiUrlTemplate;
    }

    public String getApiUrlToken() {
        return apiUrlToken;
    }

    public void setApiUrlToken(String apiUrlToken) {
        this.apiUrlToken = apiUrlToken;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getInDomain() {
        return inDomain;
    }

    public void setInDomain(String inDomain) {
        this.inDomain = inDomain;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(String periodStart) {
        this.periodStart = periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(String periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        this.outputPrefix = outputPrefix;
    }

    public String getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(String targetKey) {
        this.targetKey = targetKey;
    }
}
