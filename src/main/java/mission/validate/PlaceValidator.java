package mission.validate;

import mission.exception.MissionException;
import mission.exception.PlaceError;
import mission.model.Place;
import mission.service.PlaceService;

public final class PlaceValidator {
    private PlaceValidator() {}

    public static Place resolveOrThrow(PlaceService service, String placeName) {
        return service.findPlaceByName(placeName)
                .orElseThrow(() -> new MissionException(
                        String.format(PlaceError.NOT_FOUND.message(), placeName)
                ));
    }
}
