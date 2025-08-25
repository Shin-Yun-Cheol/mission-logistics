package mission.repository;

public interface OrderLog {
    void append(int id, String customer, String departure, String destination, int minutes);
}
