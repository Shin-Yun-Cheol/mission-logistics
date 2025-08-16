package mission.service;

import mission.exception.MissionException;
import mission.model.LatLng;
import mission.model.Place;
import mission.util.TravelTimeCalculator;
import mission.validate.CoordinateValidator;
import mission.validate.PlaceValidator;

public class DeliveryService {

    private final PlaceService placeService;
    private final double speedKmh;

    public DeliveryService(PlaceService placeService, double speedKmh) {
        this.placeService = placeService;
        this.speedKmh = speedKmh;
    }

    public int estimateMinutes(String depName, String dstName) throws MissionException {
        Place dep = PlaceValidator.resolveOrThrow(placeService, depName);
        Place dst = PlaceValidator.resolveOrThrow(placeService, dstName);

        LatLng depLL = CoordinateValidator.resolveOrThrow(placeService, dep);
        LatLng dstLL = CoordinateValidator.resolveOrThrow(placeService, dst);

        double distanceKm = TravelTimeCalculator.calculateDistanceKm(depLL, dstLL);
        return TravelTimeCalculator.estimateTravelMinutes(distanceKm, speedKmh);
    }

    /* 예상 3분당 실제 1초 → 분/3 을 올림해 초로 변환 */
    public int toRealSeconds(int estimatedMinutes) {
        return Math.max(1, (int) Math.ceil(estimatedMinutes / 3.0));
    }
}
