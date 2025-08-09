package mission.repository.csv;

import mission.model.LatLng;
import mission.repository.CoordinateRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CsvCoordinateRepository implements CoordinateRepository {

    /** place_id → LatLng 인덱스 맵 */
    private final Map<Integer, LatLng> placeIdToLatLngIndex;

    /**
     * @param csvResourceName 클래스패스 리소스 이름 (예: "position.csv")
     */
    public CsvCoordinateRepository(String csvResourceName) {
        this.placeIdToLatLngIndex = loadFromResources(csvResourceName);
    }

    private Map<Integer, LatLng> loadFromResources(String csvResourceName) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream resourceInputStream = contextClassLoader.getResourceAsStream(csvResourceName);
        if (resourceInputStream == null) {
            throw new IllegalStateException("리소스를 찾을 수 없습니다: " + csvResourceName);
        }

        Map<Integer, LatLng> indexMap = new HashMap<>();
        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(resourceInputStream, StandardCharsets.UTF_8))) {

            bufferedReader.lines()
                    .skip(1) // header: place_id,lat,lng
                    .map(line -> line.split(",", 3))
                    .forEach(columns -> {
                        if (columns.length < 3)
                            return;

                        int placeId = Integer.parseInt(columns[0].trim());
                        double latitude = Double.parseDouble(columns[1].trim());
                        double longitude = Double.parseDouble(columns[2].trim());

                        indexMap.put(placeId, new LatLng(latitude, longitude));
                    });

        } catch (Exception e) {
            throw new RuntimeException("CSV 읽기 실패: " + csvResourceName, e);
        }
        return indexMap;
    }

    @Override
    public Optional<LatLng> findByPlaceId(int placeId) {
        return Optional.ofNullable(placeIdToLatLngIndex.get(placeId));
    }
}
