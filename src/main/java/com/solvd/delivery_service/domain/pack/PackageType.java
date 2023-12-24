package com.solvd.delivery_service.domain.pack;

import com.solvd.delivery_service.util.console_menu.menu_enums.IMenu;

public enum PackageType implements IMenu {
    EXTRA_SMALL("X-Small", 0.0, 0.2, 19.5),
    SMALL("Small", 0.2, 0.5, 21.5),
    MEDIUM("Medium", 0.5, 0.9, 23.5),
    MEDIUM_PLUS("Medium+", 0.9, 1.4, 25.5),
    LARGE("Large", 1.4, 2.0, 27.5),
    LARGE_PLUS("Large+", 2.0, 5.0, 29.5),
    EXTRA_LARGE("X-Large", 5.0, 100.0, 61.3);

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
