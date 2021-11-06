package io.wisoft.vamos.util;

import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.exception.NoMatchUserInfoException;

import java.util.Optional;

public class UserUtils {

    //인스턴스화 방지
    private UserUtils() {}

    public static Optional<UserLocation> getUserLocation(User user) {
        UserLocation location = user.getLocation();
        return Optional.ofNullable(location);
    }

    public static void compareUser(User user1, User user2, String errorMessage) {
        if (user1 == null) throw new IllegalArgumentException("user must be not null.");
        if (!user1.equals(user2)) throw new NoMatchUserInfoException(errorMessage);
    }
}
