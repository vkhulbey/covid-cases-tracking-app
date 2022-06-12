package com.covidtracker.covidapp.services;

import com.covidtracker.covidapp.constants.CovidTrackerConstants;
import com.covidtracker.covidapp.models.LocationStats;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class CovidDataService {
    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * * 1 * * ")
    public void fetchCovidData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();

        Reader csvBodyReader = new StringReader(getStringHttpResponse().body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        parseResponse(newStats, records);
        this.allStats = newStats;
    }

    private void parseResponse(List<LocationStats> newStats, Iterable<CSVRecord> records) {
        records.forEach(rcrd -> {
            LocationStats locationStats = new LocationStats();
            locationStats.setState(rcrd.get(CovidTrackerConstants.PROVINCE_STATE.getValue()));
            locationStats.setCountry(rcrd.get(CovidTrackerConstants.COUNTRY.getValue()));
            int latestCases = Integer.parseInt(rcrd.get(rcrd.size() - 1));
            int previousDayCases = Integer.parseInt(rcrd.get(rcrd.size() - 2));
            locationStats.setLatestTotalCases(latestCases);
            locationStats.setDiffFromPreviousDay(latestCases - previousDayCases);
            newStats.add(locationStats);
        });
    }

    private HttpResponse<String> getStringHttpResponse() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(CovidTrackerConstants.COVID_DATA_URL.getValue()))
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public long getTotalReportedCases() {
        return allStats
                .stream()
                .mapToLong(LocationStats::getLatestTotalCases)
                .sum();
    }
}
