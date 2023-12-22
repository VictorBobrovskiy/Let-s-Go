package com.digsol.main.location;

import java.util.Collection;

public interface LocationService {

    LocationDto addLocation(LocationDto locationDto);

    Location getLocation(Long locationId);

    Collection<Location> getLocationsByLatAndLon(Double lat, Double lon);

    Collection<Location> getAllLocations(String text, String sort, Integer from, Integer size,
                                         Double lat, Double lon, Double distance);

    Location findOrCreateLocation(LocationDto locationDto);

    LocationDto updateLocation(Long locationId, LocationDto locationDto);


}
