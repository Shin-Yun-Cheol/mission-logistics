package mission;

import mission.controller.PlaceController;
import mission.repository.CoordinateRepository;
import mission.repository.PlaceRepository;
import mission.repository.csv.CsvCoordinateRepository;
import mission.repository.csv.CsvPlaceRepository;
import mission.service.PlaceService;
import mission.view.InputView;
import mission.view.OutputView;

public class AppConfig {

    private final String placesResource;      // 예: "place.csv"
    private final String coordinatesResource; // 예: "coordinates.csv"

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

    // View
    public InputView inputView() { return new InputView(); }
    public OutputView outputView() { return new OutputView(); }

    // Controller
    public PlaceController placeController() {
        return new PlaceController(placeService(), inputView(), outputView());
    }
}
