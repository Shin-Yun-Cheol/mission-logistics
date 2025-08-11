package mission.controller;

import mission.model.LatLng;
import mission.model.Place;
import mission.model.Route;
import mission.service.PlaceService;
import mission.util.InputParser;
import mission.util.TravelTimeCalculator;
import mission.view.InputView;
import mission.view.OutputView;

import java.util.Optional;

public class PlaceController {

    private static final double DEFAULT_SPEED_KMH = 60.0;

    private final PlaceService service;
    private final InputView inputView;
    private final OutputView outputView;

    public PlaceController(PlaceService service, InputView inputView, OutputView outputView) {
        this.service = service;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void runOnce() {
        String raw = inputView.read();

        Optional<Route> routeOpt = parseRouteOrReport(raw);
        if (routeOpt.isEmpty()) return;
        Route route = routeOpt.get();

        Optional<Resolved> depOpt = resolvePlaceAndCoordOrReport(route.departure());
        if (depOpt.isEmpty()) return;

        Optional<Resolved> dstOpt = resolvePlaceAndCoordOrReport(route.destination());
        if (dstOpt.isEmpty()) return;

        LatLng depLL = depOpt.get().latLng();
        LatLng dstLL = dstOpt.get().latLng();

        double distanceKm = TravelTimeCalculator.calculateDistanceKm(depLL, dstLL);
        int totalMinutes = TravelTimeCalculator.estimateTravelMinutes(distanceKm, DEFAULT_SPEED_KMH);

        outputView.printTravelTime(totalMinutes, DEFAULT_SPEED_KMH);
    }

    /** 입력 문자열을 Route로 파싱하고 실패 시 메시지를 출력합니다. */
    private Optional<Route> parseRouteOrReport(String raw) {
        try {
            return Optional.of(InputParser.parseRoute(raw));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    /** 장소 이름을 Place와 LatLng까지 해석하고, 중간 실패 시 적절한 메시지를 출력합니다. */
    private Optional<Resolved> resolvePlaceAndCoordOrReport(String placeName) {
        Optional<Place> placeOpt = service.findPlaceByName(placeName);
        if (placeOpt.isEmpty()) {
            outputView.printNameNotFound(placeName);
            return Optional.empty();
        }
        Place place = placeOpt.get();

        Optional<LatLng> latLngOpt = service.findLatLngByPlaceId(place.id());
        if (latLngOpt.isEmpty()) {
            outputView.printCoordNotFound(place.name(), place.id());
            return Optional.empty();
        }
        return Optional.of(new Resolved(place, latLngOpt.get()));
    }

    /** 내부 전달 객체: 조회된 Place와 좌표를 함께 보관 */
    private record Resolved(Place place, LatLng latLng) {}
}
