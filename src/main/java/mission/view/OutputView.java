package mission.view;

public class OutputView {

    public void printFound(String name, int id, String address, double lat, double lng) {
        System.out.printf("이름: %s (id=%d)%n", name, id);
        System.out.printf("주소: %s%n", address);
        System.out.printf("좌표: lat=%.7f, lng=%.7f%n", lat, lng);
    }

    public void printNameNotFound(String name) {
        System.out.printf("이름 '%s' 을(를) 찾을 수 없습니다.%n", name);
    }

    public void printCoordNotFound(String name, int id) {
        System.out.printf("'%s'(id=%d)의 좌표 정보를 찾을 수 없습니다.%n", name, id);
    }
}
