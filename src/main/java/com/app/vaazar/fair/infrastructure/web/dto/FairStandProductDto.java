package com.app.vaazar.fair.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class FairStandProductDto {
    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;

    public FairStandProductDto() {
    }

    public FairStandProductDto(UUID id, UUID productId, String name, String description, BigDecimal price, Integer stock, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
