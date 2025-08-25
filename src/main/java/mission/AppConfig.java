package mission;

import mission.controller.DeliveryController;
import mission.controller.PlaceController;
import mission.repository.CoordinateRepository;
import mission.repository.OrderLog;
import mission.repository.PlaceRepository;
import mission.repository.csv.CsvCoordinateRepository;
import mission.repository.csv.CsvPlaceRepository;
import mission.repository.file.PlainOrderLog;
import mission.service.DeliveryService;
import mission.service.PlaceService;
import mission.view.InputView;
import mission.view.OutputView;

import java.nio.file.Paths;

public class AppConfig {

    private final String placesResource;
    private final String coordinatesResource;

    public AppConfig(String placesResource, String coordinatesResource) {
        this.placesResource = placesResource;
        this.coordinatesResource = coordinatesResource;
    }

    // Repository
    public PlaceRepository placeRepository() {
        return new CsvPlaceRepository(placesResource);
    }
    public CoordinateRepository coordinateRepository() {
        return new CsvCoordinateRepository(coordinatesResource);
    }

    // Service
    public PlaceService placeService() {
        return new PlaceService(placeRepository(), coordinateRepository());
    }
    public DeliveryService deliveryService() { return new DeliveryService(placeService(), 30.0);
    }
    // View
    public InputView inputView() { return new InputView(); }
    public OutputView outputView() { return new OutputView(); }

    // Controller
    public PlaceController placeController() {
        return new PlaceController(placeService(), inputView(), outputView());
    }

    public OrderLog orderLog() {
        String path = Paths.get(System.getProperty("user.home"),".mission-logistics","orders.txt").toString();
        return new PlainOrderLog(path);
    }
    public DeliveryController deliveryController() { return new DeliveryController(deliveryService(), inputView(), outputView(), orderLog());
    }
}
