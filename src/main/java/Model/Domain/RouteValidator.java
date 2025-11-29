package Model.Domain;

public class RouteValidator {

    public static boolean isValid(RouteInput input) {
        return input.city() != null && !input.city().isEmpty() &&
                input.start() != null && !input.start().isEmpty() &&
                input.end() != null && !input.end().isEmpty();
    }
}
