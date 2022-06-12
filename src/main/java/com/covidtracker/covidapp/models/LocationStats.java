package com.covidtracker.covidapp.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationStats {

    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPreviousDay;
}
