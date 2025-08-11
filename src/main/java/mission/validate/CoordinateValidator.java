package mission.validate;

import mission.exception.MissionException;
import mission.exception.CoordinateError;
import mission.model.LatLng;
import mission.model.Place;
import mission.service.PlaceService;

public final class CoordinateValidator {
    private CoordinateValidator() {}

    public static LatLng resolveOrThrow(PlaceService service, Place place) {
        return service.findLatLngByPlaceId(place.id())
                .orElseThrow(() -> new MissionException(
                        String.format(CoordinateError.NOT_FOUND.message(), place.name(), place.id())
                ));
    }
}
