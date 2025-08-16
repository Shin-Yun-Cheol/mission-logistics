package mission.util;

import mission.model.Route;
import mission.model.DeliveryOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static DeliveryOrder parseDelivery(String input) {
        if (input == null) {
            throw new IllegalArgumentException("입력값이 없습니다.");
        }
        Pattern p = Pattern.compile("^\\s*(.+?)\\s*-\\s*(.+?)\\s*\\((.+?)\\)\\s*$");
        Matcher m = p.matcher(input);

        if (!m.matches()) {
            throw new IllegalArgumentException("입력 형식이 올바르지 않습니다. 예) 봉화군청-숭실대학교 정보과학관(홍길동)");
        }

        String dep = m.group(1).trim();
        String dst = m.group(2).trim();
        String customer = m.group(3).trim();

        if (dep.isEmpty() || dst.isEmpty() || customer.isEmpty()) {
            throw new IllegalArgumentException("출발지/도착지/주문자를 모두 입력해야 합니다.");
        }

        return new DeliveryOrder(dep, dst, customer);
    }
}
