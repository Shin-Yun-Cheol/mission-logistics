package mission.repository.csv;

import mission.model.Place;
import mission.repository.PlaceRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CsvPlaceRepository implements PlaceRepository {

    /** 정규화된 장소명 → Place 인덱스 맵 */
    private final Map<String, Place> normalizedNameToPlaceIndex;

    /**
     * @param csvResourceName 클래스패스 리소스 이름 (예: "place.csv")
     */
    public CsvPlaceRepository(String csvResourceName) {
        this.normalizedNameToPlaceIndex = loadFromResources(csvResourceName);
    }

    private Map<String, Place> loadFromResources(String csvResourceName) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream resourceInputStream = contextClassLoader.getResourceAsStream(csvResourceName);
        if (resourceInputStream == null) {
            throw new IllegalStateException("리소스를 찾을 수 없습니다: " + csvResourceName);
        }

        Map<String, Place> indexMap = new HashMap<>();
        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(resourceInputStream, StandardCharsets.UTF_8))) {

            bufferedReader.lines()
                    .skip(1) // header: id,name,address
                    .map(line -> line.split(",", 3)) // address에 콤마가 있어도 최대 3조각까지만 분리
                    .forEach(columns -> {
                        if (columns.length < 3)
                            return;

                        int placeId = Integer.parseInt(columns[0].trim());
                        String placeName = columns[1].trim();
                        String placeAddress = columns[2].trim();

                        String normalizedKey = normalizeName(placeName);
                        indexMap.put(normalizedKey, new Place(placeId, placeName, placeAddress));
                    });
        } catch (Exception e) {
            throw new RuntimeException("CSV 읽기 실패: " + csvResourceName, e);
        }
        return indexMap;
    }

    /**
     * 장소명 정규화:
     * - 유니코드 NFC 정규화 (한글 조합/분해 차이 제거)
     * - 앞뒤 공백 제거 및 연속 공백을 단일 공백으로 축약
     */
    private static String normalizeName(String inputName) {
        if (inputName == null) {
            return "";
        }
        String nfcNormalized = Normalizer.normalize(inputName, Normalizer.Form.NFC);
        return nfcNormalized.trim().replaceAll("\\s+", " ");
    }

    @Override
    public Optional<Place> findByName(String rawName) {
        String normalizedKey = normalizeName(rawName);
        return Optional.ofNullable(normalizedNameToPlaceIndex.get(normalizedKey));
    }
}
