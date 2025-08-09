package mission;

public class Application {
    public static void main(String[] args) {
        AppConfig config = new AppConfig("place.csv", "position.csv");
        config.placeController().runOnce();
    }
}
