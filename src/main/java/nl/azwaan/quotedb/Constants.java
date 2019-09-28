package nl.azwaan.quotedb;

/**
 * Class defining application-wide constants.
 *
 * @author Aron Zwaan
 */
public final class Constants {

    /**
     * Key to hash/sign JWT tokens with.
     */
    public static final String JWT_HASH_KEY = "auth.jwt.hash.key";

    /**
     * JWT property key for storing user id in token.
     */
    public static final String JWT_USER_ID_KEY = "user_id";

    private Constants() { }

    /**
     * Maximum page size.
     */
    public static final int MAX_PAGE_SIZE = 100;

}
