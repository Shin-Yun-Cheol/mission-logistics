package mission.controller;

import mission.model.LatLng;
import mission.model.Place;
import mission.model.Route;
import mission.service.PlaceService;
import mission.util.InputParser;
import mission.util.TravelTimeCalculator;   // ✅ 추가
import mission.view.InputView;
import mission.view.OutputView;

import java.util.Optional;

public class PlaceController {
    private final PlaceService service;
    private final InputView inputView;
    private final OutputView outputView;

    public PlaceController(PlaceService service, InputView inputView, OutputView outputView) {
        this.service = service;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void runOnce() {
        // 1) 입력 받기 ("출발지,도착지")
        String raw = inputView.read();

        // 2) 파싱 -> Route(departure, destination)
        Route route;
        try {
            route = InputParser.parseRoute(raw);
        } catch (IllegalArgumentException e) {
            // 형식 오류 메시지 그대로 보여주고 종료
            System.out.println(e.getMessage());
            return;
        }

        // 출발/도착 각각 Place 조회
        Optional<Place> depOpt = service.findPlaceByName(route.departure());
        if (depOpt.isEmpty()) {
            outputView.printNameNotFound(route.departure());
            return;
        }
        Optional<Place> dstOpt = service.findPlaceByName(route.destination());
        if (dstOpt.isEmpty()) {
            outputView.printNameNotFound(route.destination());
            return;
        }

        Place dep = depOpt.get();
        Place dst = dstOpt.get();

        // 각 Place의 좌표 조회
        Optional<LatLng> depLatLngOpt = service.findLatLngByPlaceId(dep.id());
        if (depLatLngOpt.isEmpty()) {
            outputView.printCoordNotFound(dep.name(), dep.id());
            return;
        }
        Optional<LatLng> dstLatLngOpt = service.findLatLngByPlaceId(dst.id());
        if (dstLatLngOpt.isEmpty()) {
            outputView.printCoordNotFound(dst.name(), dst.id());
            return;
        }

        LatLng depLL = depLatLngOpt.get();
        LatLng dstLL = dstLatLngOpt.get();


        // 이동 시간 계산(60km/h) → OutputView에서 h:nn으로 포맷 출력
        double distanceKm = TravelTimeCalculator.calculateDistanceKm(depLL, dstLL);
        int totalMinutes = TravelTimeCalculator.estimateTravelMinutes(distanceKm, 60.0);

        outputView.printTravelTime(totalMinutes, 60.0);
    }
}
