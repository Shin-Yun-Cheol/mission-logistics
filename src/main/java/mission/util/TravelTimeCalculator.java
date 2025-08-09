package mission.util;

import mission.model.LatLng;

public final class TravelTimeCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private TravelTimeCalculator() {}

    /** Haversine 공식으로 두 좌표 간 대권거리(km) 계산 */
    public static double calculateDistanceKm(LatLng from, LatLng to) {
        double lat1Rad = Math.toRadians(from.latitude());
        double lon1Rad = Math.toRadians(from.longitude());
        double lat2Rad = Math.toRadians(to.latitude());
        double lon2Rad = Math.toRadians(to.longitude());

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(dLat / 2.0), 2.0)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2.0), 2.0);
        double c = 2.0 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS_KM * c;
    }

    /** 거리(km)와 속도(km/h)로 총 소요시간(분)을 반올림하여 반환 */
    public static int estimateTravelMinutes(double distanceKm, double speedKmh) {
        if (speedKmh <= 0.0) {
            throw new IllegalArgumentException("속도는 0보다 커야 합니다.");
        }
        double hours = distanceKm / speedKmh;
        long roundedMinutes = Math.round(hours * 60.0);
        // int로 안전 변환 (일반 범위에서는 오버플로우 우려 없음)
        return (int) roundedMinutes;
    }

    /** 편의 메서드: 좌표와 속도로 직접 분(minute) 계산 */
    public static int estimateTravelMinutes(LatLng from, LatLng to, double speedKmh) {
        double distanceKm = calculateDistanceKm(from, to);
        return estimateTravelMinutes(distanceKm, speedKmh);
    }
}
