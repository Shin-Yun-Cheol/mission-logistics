package mission.controller;

import mission.exception.MissionException;
import mission.model.LatLng;
import mission.model.Place;
import mission.model.Route;
import mission.service.PlaceService;
import mission.util.TravelTimeCalculator;
import mission.view.InputView;
import mission.view.OutputView;
import mission.validate.CoordinateValidator;
import mission.validate.PlaceValidator;
import mission.validate.RouteValidator;

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
        try {
            String raw = inputView.read();

            Route route = RouteValidator.parseOrThrow(raw);

            Place dep = PlaceValidator.resolveOrThrow(service, route.departure());
            Place dst = PlaceValidator.resolveOrThrow(service, route.destination());

            LatLng depLL = CoordinateValidator.resolveOrThrow(service, dep);
            LatLng dstLL = CoordinateValidator.resolveOrThrow(service, dst);

            double distanceKm = TravelTimeCalculator.calculateDistanceKm(depLL, dstLL);
            int minutes = TravelTimeCalculator.estimateTravelMinutes(distanceKm, DEFAULT_SPEED_KMH);

            outputView.printTravelTime(minutes, DEFAULT_SPEED_KMH);

        } catch (MissionException ex) {
            outputView.printError(ex.getMessage());
        }
    }
}
