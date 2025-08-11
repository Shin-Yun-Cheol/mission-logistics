package mission.exception;

public enum RouteError {
    INVALID_FORMAT("입력 형식이 잘못되었습니다. '출발지,도착지' 형태여야 합니다."),
    MISSING_DEPARTURE("출발지가 비어 있습니다."),
    MISSING_DESTINATION("도착지가 비어 있습니다.");

    private final String message;

    RouteError(String message) {
        this.message = message;
    }
    public String message() {
        return message;
    }
}
