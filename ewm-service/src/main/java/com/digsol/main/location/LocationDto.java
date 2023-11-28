package com.digsol.main.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationDto {

    private Long id;

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @Size(min = 2, max = 255)
    private String name;

    private AddressDto address;

    @Size(max = 1000)
    private String description;

    @Override
    public String toString() {
        return "LocationDto{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", description='" + description + '\'' +
                '}';
    }
}