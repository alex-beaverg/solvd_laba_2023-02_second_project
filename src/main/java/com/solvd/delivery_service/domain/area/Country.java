package com.solvd.delivery_service.domain.area;

public enum Country {
    BELARUS("Belarus", Zone.ZONE_A),
    POLAND("Poland", Zone.ZONE_B),
    LITHUANIA("Lithuania", Zone.ZONE_D),
    GERMANY("Germany", Zone.ZONE_C);

    private final String title;
    private final Zone zone;

    Country(String title, Zone zone) {
        this.title = title;
        this.zone = zone;
    }

    public String getTitle() {
        return title;
    }

    public Zone getZone() {
        return zone;
    }
}
