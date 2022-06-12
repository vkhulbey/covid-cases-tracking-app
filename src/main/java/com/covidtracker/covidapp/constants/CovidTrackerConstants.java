package com.covidtracker.covidapp.constants;

public enum CovidTrackerConstants {
    PROVINCE_STATE("Province/State"),
    COVID_DATA_URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv"),
    COUNTRY("Country/Region");

    final String value;

    CovidTrackerConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
