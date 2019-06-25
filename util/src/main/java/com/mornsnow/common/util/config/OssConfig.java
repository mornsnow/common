package com.mornsnow.common.util.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="oss")
public class OssConfig implements BaseOSSConfig{
    private  Integer connectionTimeout;
     private  Integer socketTimeout;
     private  Integer maxErrorRetry;
     private  Integer maxConnections;
     private   String endpoint;
     private   String accessKeyId;
     private   String accessKeySecret;
    private  String bucketName;
    private    String fileExpireTimeLong;
    private    String fileExpireTimeUnit;
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }
    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    public Integer getSocketTimeout() {
        return socketTimeout;
    }
    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
    public Integer getMaxErrorRetry() {
        return maxErrorRetry;
    }
    public void setMaxErrorRetry(Integer maxErrorRetry) {
        this.maxErrorRetry = maxErrorRetry;
    }
    public Integer getMaxConnections() {
        return maxConnections;
    }
    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    public String getAccessKeyId() {
        return accessKeyId;
    }
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
    public String getBucketName() {
        return bucketName;
    }
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    public String getFileExpireTimeLong() {
        return fileExpireTimeLong;
    }
    public void setFileExpireTimeLong(String fileExpireTimeLong) {
        this.fileExpireTimeLong = fileExpireTimeLong;
    }
    public String getFileExpireTimeUnit() {
        return fileExpireTimeUnit;
    }
    public void setFileExpireTimeUnit(String fileExpireTimeUnit) {
        this.fileExpireTimeUnit = fileExpireTimeUnit;
    }
    @Override
    public String getSecurityToken() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setSecurityToken(String securityToken) {
        // TODO Auto-generated method stub
        
    }
}
