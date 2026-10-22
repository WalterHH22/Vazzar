package com.app.vaazar.fair.infrastructure.web.dto;

import java.util.List;
import java.util.UUID;

public class FairStandResponse {
    private UUID id;
    private String standNumber;
    private String name;
    private String merchantName;
    private String contact;
    private String pavilion;
    private String canopyColor;
    private String description;
    private String logoUrl;
    private List<FairStandProductDto> products;

    public FairStandResponse() {
    }

    public FairStandResponse(UUID id, String standNumber, String name, String merchantName, String contact,
                             String pavilion, String canopyColor, String description, String logoUrl,
                             List<FairStandProductDto> products) {
        this.id = id;
        this.standNumber = standNumber;
        this.name = name;
        this.merchantName = merchantName;
        this.contact = contact;
        this.pavilion = pavilion;
        this.canopyColor = canopyColor;
        this.description = description;
        this.logoUrl = logoUrl;
        this.products = products;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public List<FairStandProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<FairStandProductDto> products) {
        this.products = products;
    }
}
