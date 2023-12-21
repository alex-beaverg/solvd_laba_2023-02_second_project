package com.solvd.delivery_service.domain.area;

public enum Zone {
    ZONE_A("Zone A", 1, 1),
    ZONE_B("Zone B", 1, 2),
    ZONE_C("Zone C", 1, 3),
    ZONE_D("Zone D", 2, 1),
    ZONE_E("Zone E", 2, 2),
    ZONE_F("Zone F", 2, 3),
    ZONE_G("Zone G", 3, 1),
    ZONE_H("Zone H", 3, 2),
    ZONE_I("Zone I", 3, 3);

    private final String title;
    private final Integer indexX;
    private final Integer indexY;

    Zone(String title, Integer indexX, Integer indexY) {
        this.title = title;
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIndexX() {
        return indexX;
    }

    public Integer getIndexY() {
        return indexY;
    }
}
