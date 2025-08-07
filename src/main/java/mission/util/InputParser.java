package mission.util;

import mission.model.Route;

public class InputParser {

    public static Route parseRoute(String input) {
        if (input == null || !input.contains(",")) {
            throw new IllegalArgumentException("입력 형식이 올바르지 않습니다. 예: '서울,부산'");
        }

        String[] parts = input.split(",", 2);

        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException("출발지와 도착지를 모두 입력해야 합니다.");
        }

        return new Route(parts[0].trim(), parts[1].trim());
    }
}
