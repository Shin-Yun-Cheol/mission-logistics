package mission.repository;

import java.util.List;

public interface OrderLog {
    void append(int id, String customer, String departure, String destination, int minutes);
    List<String> findByCustomerLines(String keyword);
}
