package io.wisoft.vamos.util;

import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;

import java.util.Optional;

public class UserUtils {

    public static Optional<UserLocation> getUserLocation(User user) {
        UserLocation location = user.getLocation();
        return Optional.ofNullable(location);
    }
}
