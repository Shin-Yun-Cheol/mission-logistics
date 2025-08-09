package mission.repository;

import mission.model.Place;
import java.util.Optional;

public interface PlaceRepository {
    Optional<Place> findByName(String name);
}
