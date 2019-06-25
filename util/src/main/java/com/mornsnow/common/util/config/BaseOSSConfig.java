package com.mornsnow.common.util.config;

public interface BaseOSSConfig {
    public Integer getConnectionTimeout();
    public void setConnectionTimeout(Integer connectionTimeout) ;
    public Integer getSocketTimeout() ;
    public void setSocketTimeout(Integer socketTimeout) ;
    public Integer getMaxErrorRetry() ;
    public void setMaxErrorRetry(Integer maxErrorRetry);
    public Integer getMaxConnections();
    public void setMaxConnections(Integer maxConnections);
    public String getEndpoint() ;
    public void setEndpoint(String endpoint);
    public String getAccessKeyId() ;
    public void setAccessKeyId(String accessKeyId);
    public String getAccessKeySecret() ;
    public void setAccessKeySecret(String accessKeySecret) ;
    public String getBucketName();
    public void setBucketName(String bucketName) ;
    public String getFileExpireTimeLong();
    public void setFileExpireTimeLong(String fileExpireTimeLong)  ;
    public String getFileExpireTimeUnit()  ;
    public void setFileExpireTimeUnit(String fileExpireTimeUnit) ;
    public String getSecurityToken();
    public void setSecurityToken(String securityToken) ;
}
