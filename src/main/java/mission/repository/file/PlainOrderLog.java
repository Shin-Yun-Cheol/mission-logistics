package mission.repository.file;

import mission.repository.OrderLog;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.locks.ReentrantLock;

public class PlainOrderLog implements OrderLog {

    private static final String HEADER =
            "주문 id |주문자        |배송 시작 장소      |배송 도착 장소      |소요 시간";

    private final Path file;
    private final ReentrantLock lock = new ReentrantLock(true);

    public PlainOrderLog(String filePath) {
        this.file = Paths.get(filePath);
        ensureFile();
    }

    private void ensureFile() {
        try {
            if (Files.notExists(file)) {
                Path parent = file.getParent();
                if (parent != null)
                    Files.createDirectories(parent);
                Files.writeString(file, HEADER + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String fmtDuration(int minutes) {
        int h = minutes / 60, m = minutes % 60;
        return String.format("%d시간 %d분", h, m);
    }

    @Override
    public void append(int id, String customer, String dep, String dst, int minutes) {
        String line = String.format("%-6d |%-12s |%-16s |%-16s |%s",
                id, customer, dep, dst, fmtDuration(minutes));
        lock.lock();
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            lock.unlock();
        }
    }
}
