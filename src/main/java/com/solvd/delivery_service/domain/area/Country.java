package com.solvd.delivery_service.domain.area;

import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;

public enum Country implements IMenu {
    BELARUS("Belarus", Zone.ZONE_A),
    POLAND("Poland", Zone.ZONE_B),
    LITHUANIA("Lithuania", Zone.ZONE_D),
    LATVIA("Latvia", Zone.ZONE_D),
    ESTONIA("Estonia", Zone.ZONE_G),
    FINLAND("Finland", Zone.ZONE_H),
    SWEDEN("Sweden", Zone.ZONE_I),
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

    public String getName() {
        return this.name();
    }
}
