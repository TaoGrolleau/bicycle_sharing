package spring.main.models;

import lombok.Data;

@Data
public class BicycleStation {
    private String station_name;
    private String station_id;
    private String station_lon;
    private String station_lat;
    private String station_available_bikes;
    private String station_used_bikes;
    private String station_total_bikes;
}
