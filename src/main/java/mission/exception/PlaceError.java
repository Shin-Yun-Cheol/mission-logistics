package mission.exception;

public enum PlaceError {
    NOT_FOUND("존재하지 않는 장소입니다: %s");

    private final String message;

    PlaceError(String message) {
        this.message = message;
    }
    public String message() {
        return message;
    }
}
