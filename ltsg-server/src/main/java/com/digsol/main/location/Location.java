package com.digsol.main.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "description")
    private String description;

    public Location(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Location(Double lat, Double lon, String name, Address address, String description) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.address = address;
        this.description = description;
    }


    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}