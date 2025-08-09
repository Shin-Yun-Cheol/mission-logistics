package mission.repository;

import mission.model.LatLng;
import java.util.Optional;

public interface CoordinateRepository {
    Optional<LatLng> findByPlaceId(int placeId);
}
