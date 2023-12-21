package com.solvd.delivery_service.domain.pack;

public enum PackageType {
    EXTRA_SMALL("Extra small package", 0.0, 0.2, 19.5),
    SMALL("Small package", 0.2, 0.5, 21.5),
    MEDIUM("Medium package", 0.5, 0.9, 23.5),
    MEDIUM_PLUS("Medium+ package", 0.9, 1.4, 25.5),
    LARGE("Large package", 1.4, 2.0, 27.5),
    LARGE_PLUS("Large+ package", 2.0, 5.0, 29.5),
    EXTRA_LARGE("Extra large package", 5.0, 100.0, 61.3);

    private final String title;
    private final Double minWeight;
    private final Double maxWeight;
    private final Double baseCost;

    PackageType(String title, Double minWeight, Double maxWeight, Double baseCost) {
        this.title = title;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.baseCost = baseCost;
    }

    public String getTitle() {
        return title;
    }

    public Double getMinWeight() {
        return minWeight;
    }

    public Double getMaxWeight() {
        return maxWeight;
    }

    public Double getBaseCost() {
        return baseCost;
    }
}
