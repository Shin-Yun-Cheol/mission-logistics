package mission.view;

public class OutputView {

    public void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void printTravelTime(int totalMinutes, double speedKmh) {
        int h = totalMinutes / 60;
        int m = totalMinutes % 60;
        System.out.printf("이동 시간(%.0f km/h 기준): %d:%02d%n", speedKmh, h, m);
    }
}
