package com.solvd.delivery_service.domain.pack;

public enum DeliveryType {
    REGULAR("Regular delivery", 3, 1.5),
    EXPRESS("Express delivery", 2, 4.5),
    VIP("VIP delivery", 1, 13.5);

    private final String title;
    private final Integer daysCountPerZone;
    private final Double costFactor;

    DeliveryType(String title, Integer daysCountPerZone, Double costFactor) {
        this.title = title;
        this.daysCountPerZone = daysCountPerZone;
        this.costFactor = costFactor;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDaysCountPerZone() {
        return daysCountPerZone;
    }

    public Double getCostFactor() {
        return costFactor;
    }
}
