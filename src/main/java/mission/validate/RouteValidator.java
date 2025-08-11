package mission.validate;

import mission.exception.MissionException;
import mission.exception.RouteError;
import mission.model.Route;
import mission.util.InputParser;

public final class RouteValidator {
    private RouteValidator() {}

    public static Route parseOrThrow(String raw) {
        try {
            Route r = InputParser.parseRoute(raw);

            if (r.departure().isBlank())
                throw new MissionException(RouteError.MISSING_DEPARTURE.message());

            if (r.destination().isBlank())
                throw new MissionException(RouteError.MISSING_DESTINATION.message());

            return r;
        } catch (IllegalArgumentException e) {
            throw new MissionException(RouteError.INVALID_FORMAT.message());
        }
    }
}
