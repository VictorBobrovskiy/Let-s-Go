package com.digsol.main.location;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LocationMapper {

    public static Location toLocation(LocationDto locationDto) {
        return new Location(
                locationDto.getLat(),
                locationDto.getLon(),
                locationDto.getName() == null ? "" : locationDto.getName(),
                locationDto.getAddress() == null ? null : AddressMapper.toAddress(locationDto.getAddress()),
                locationDto.getDescription()
        );

    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getId(),
                location.getLat(),
                location.getLon(),
                location.getName() == null ? "" : location.getName(),
                location.getAddress() == null ? null : AddressMapper.toAddressDto(location.getAddress()),
                location.getDescription()
        );
    }

    public static LocationShortDto toLocationShortDto(Location location) {

        String name;

        if (location.getName() == null && location.getAddress() == null && location.getDescription() == null) {
            name = location.getLat().toString() + ", " + location.getLon().toString();

        } else if (location.getName() == null && location.getAddress() != null) {
            name = location.getAddress().toString();

        } else name = location.getName();

        return new LocationShortDto(name);
    }
}