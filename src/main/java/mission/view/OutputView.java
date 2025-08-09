// src/main/java/mission/view/OutputView.java
package mission.view;

public class OutputView {

    public void printFound(String name, int id, String address, double latitude, double longitude) {
        System.out.printf("[FOUND] name=%s, id=%d, address=%s, lat=%.7f, lng=%.7f%n",
                name, id, address, latitude, longitude);
    }

    public void printNameNotFound(String name) {
        System.out.println("[NOT FOUND] place name: " + name);
    }

    public void printCoordNotFound(String name, int id) {
        System.out.println("[COORD NOT FOUND] name=" + name + ", id=" + id);
    }

    /** totalMinutes를 받아 h:nn 형태로 포맷하여 출력 (포맷 책임은 View) */
    public void printTravelTime(int totalMinutes, double speedKmh) {
        if (totalMinutes < 0) {
            System.out.println("[WARN] 음수 시간은 표시할 수 없습니다.");
            return;
        }
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        System.out.printf("이동 시간(%.0f km/h 기준): %d:%02d%n", speedKmh, hours, minutes);
    }
}
