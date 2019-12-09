package spring.main.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class BicycleStation {
    @Id
    @GeneratedValue
    private Long id;
    private String station_name;
    private String station_id;
    private String station_lon;
    private String station_lat;
    private String station_available_bikes;
    private String station_used_bikes;
    private String station_total_bikes;
    private String city;

    public BicycleStation(){
    }

    public BicycleStation(String station_name, String station_id, String station_lon, String station_lat, String station_available_bikes, String station_used_bikes, String station_total_bikes, String city) {
        this.station_name = station_name;
        this.station_id = station_id;
        this.station_lon = station_lon;
        this.station_lat = station_lat;
        this.station_available_bikes = station_available_bikes;
        this.station_used_bikes = station_used_bikes;
        this.station_total_bikes = station_total_bikes;
        this.city = city;
    }
}
