package com.prithvi.youtility.model;

import lombok.Data;

import java.util.List;

@Data
public class MasterData {
    private List<String> amenities;
    private List<String> propertyType;
    private List<String> locations;
    private List<String> energyRatings;
}
