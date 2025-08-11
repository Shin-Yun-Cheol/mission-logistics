package mission.exception;

public enum CoordinateError {
    NOT_FOUND("좌표를 찾을 수 없습니다: name=%s, id=%d");

    private final String message;

    CoordinateError(String message) {
        this.message = message;
    }
    public String message() {
        return message;
    }
}
