package mission.service;

import mission.model.LatLng;
import mission.model.Place;
import mission.repository.CoordinateRepository;
import mission.repository.PlaceRepository;

import java.util.Optional;

public class PlaceService {
    private final PlaceRepository placeRepository;
    private final CoordinateRepository coordinateRepository;

    public PlaceService(PlaceRepository placeRepository, CoordinateRepository coordinateRepository) {
        this.placeRepository = placeRepository;
        this.coordinateRepository = coordinateRepository;
    }

    public Optional<Place> findPlaceByName(String name) {
        return placeRepository.findByName(name);
    }

    public Optional<LatLng> findLatLngByPlaceId(int placeId) {
        return coordinateRepository.findByPlaceId(placeId);
    }

    public Optional<LatLng> findLatLngByName(String name) {
        return findPlaceByName(name).flatMap(p -> coordinateRepository.findByPlaceId(p.id()));
    }
}
