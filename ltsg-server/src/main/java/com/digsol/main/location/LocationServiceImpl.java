package com.digsol.main.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final AddressRepository addressRepository;


    @Transactional
    @Override
    public LocationDto addLocation(LocationDto locationDto) {
        Location location = findOrCreateLocation(locationDto);

        if (location.getAddress() == null) {
            Address address = getAddressByLatAndLong(locationDto.getLat(), locationDto.getLon());

            if (address != null) {

                location.setAddress(address);
                addressRepository.save(address);
            }
        } else {
            addressRepository.save(location.getAddress());
            locationRepository.save(location);
        }
        return LocationMapper.toLocationDto(location);
    }


    @Transactional
    @Override
    public LocationDto updateLocation(Long locationId, LocationDto locationDto) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException("Location " + locationId + " not found"));

        if (locationDto.getName() != null && !locationDto.getName().isBlank()) {
            location.setName(locationDto.getName());
        }

        if (locationDto.getLat() != null) {
            location.setLat(locationDto.getLat());
        }

        if (locationDto.getLon() != null) {
            location.setLon(locationDto.getLon());
        }

        if (locationDto.getAddress() != null) {
            location.setAddress(AddressMapper.toAddress(locationDto.getAddress()));
        }

        if (locationDto.getDescription() != null) {
            location.setDescription(locationDto.getDescription());
        }

        Location updatedLocation = locationRepository.save(location);
        return LocationMapper.toLocationDto(updatedLocation);
    }


    @Override
    public Location getLocation(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException("Location " + locationId + " not found"));
    }

    @Override
    public Collection<Location> getLocationsByLatAndLon(Double lat, Double lon) {
        return locationRepository.findAllByLatAndLon(lat, lon);
    }

    @Override
    public Collection<Location> getAllLocations(String text, String sort, Integer from, Integer size,
                                                Double lat, Double lon, Double distance) {

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Location> locationPage;

        if (text != null) {
            locationPage = locationRepository.findAllByNameContainingIgnoreCase(text, pageable);

        } else if (distance != null && lat != null && lon != null) {
            locationPage = locationRepository.findAllByDistanceOrderByDistanceAsc(lat, lon, distance, pageable);

        } else if ("distance".equalsIgnoreCase(sort) && lat != null && lon != null) {
            locationPage = locationRepository.findAllOrderByDistanceAsc(lat, lon, pageable);

        } else {
            locationPage = locationRepository.findAllOrderByName(pageable);
        }

        return locationPage.toList();
    }


    private Address getAddressByLatAndLong(Double lat, Double lon) {
        try {
            return GeoCoder.geocode(lat, lon);
        } catch (Exception e) {
            log.error("Geocoding error" + e.getMessage() + e.getClass() + e.getCause());
        }
        return null;
    }


    @Override
    public Location findOrCreateLocation(LocationDto locationDto) {

        if (locationDto == null ||
                ((locationDto.getLat() == null || locationDto.getLon() == null)
                        && locationDto.getName() == null)) {
            return null;
        }
        if (locationDto.getLat() != null || locationDto.getLon() != null) {
            List<Location> locations = new ArrayList<>(
                    locationRepository.findAllByLatAndLon(locationDto.getLat(), locationDto.getLon()));

            if (!locations.isEmpty()) {
                if (locationDto.getName() != null) {
                    String name = locationDto.getName();
                    for (Location location : locations) {
                        if (location.getName().equals(name)) {
                            return location;
                        }
                    }
                } else if (locations.size() == 1) {
                    return locations.get(0);
                }
            }
        } else if (!locationDto.getName().isBlank()) {
            return locationRepository.findByName(locationDto.getName())
                    .orElseGet(() -> locationRepository.save(LocationMapper.toLocation(locationDto)));
        }
        return locationRepository.save(LocationMapper.toLocation(locationDto));
    }

}

