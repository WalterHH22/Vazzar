package com.app.vaazar.fair.infrastructure.web.dto;

public class CreateOrUpdateStandRequest {
    private String merchantEmail;
    private String merchantName;
    private String standNumber;
    private String name;
    private String pavilion;
    private String canopyColor;
    private String description;
    private String logoUrl;

    public CreateOrUpdateStandRequest() {
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getStandNumber() {
        return standNumber;
    }

    public void setStandNumber(String standNumber) {
        this.standNumber = standNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPavilion() {
        return pavilion;
    }

    public void setPavilion(String pavilion) {
        this.pavilion = pavilion;
    }

    public String getCanopyColor() {
        return canopyColor;
    }

    public void setCanopyColor(String canopyColor) {
        this.canopyColor = canopyColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
