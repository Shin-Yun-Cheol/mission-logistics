package mission.view;

import java.util.List;

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

    public void printSearchHeader() {
        System.out.println();
        System.out.println("주문 id |주문자        |배송 시작 장소      |배송 도착 장소      |소요 시간      |");
    }
    public void printSearchRows(List<String> rows) {
        if (rows == null || rows.isEmpty()) {
            System.out.println("(검색 결과가 없습니다)");
            return;
        }
        for (String row : rows)
            System.out.println(row);
    }
}
