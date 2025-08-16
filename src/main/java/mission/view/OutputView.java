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

    public void printAccepted(int id) {
        System.out.printf("배송이 정상적으로 접수되었습니다. (id: %d)%n", id);
    }

    public void printStarted(int id, int totalMinutes) {
        int h = totalMinutes / 60;
        int m = totalMinutes % 60;
        System.out.printf("배송이 시작되었습니다. (id: %d, 예상 배송 시간 : %d시간 %d분)%n", id, h, m);
    }

    public void printCompleted(int id) {
        System.out.printf("배송이 완료되었습니다. (id: %d)%n", id);
    }

}
