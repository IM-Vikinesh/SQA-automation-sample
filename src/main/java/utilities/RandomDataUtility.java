package utilities;

import java.util.Random;

/*
 * RandomDataUtility generates random test data.
 *
 * Why random data?
 * The Demo Web Shop doesn't allow duplicate email registrations.
 * If I used "test@test.com" every time, the second test run would fail.
 * Using System.currentTimeMillis() guarantees a unique email every run.
 *
 * I also randomize names and phone numbers to make test data more realistic.
 */
public class RandomDataUtility {

    private static final Random random = new Random();
    private static final String[] NAMES = {"user1", "user2", "user3", "user4", "user5", "user6"};

    /*
     * Generates email like: user1715000000000@test.com
     * currentTimeMillis() returns the number of milliseconds since Jan 1, 1970.
     * Since it's always increasing, every call gets a unique value.
     */
    public static String generateEmail() {
        return "user" + System.currentTimeMillis() + "@test.com";
    }

    // Random name like "Alex742" or "Sarah123"
    public static String generateName() {
        return NAMES[random.nextInt(NAMES.length)] + random.nextInt(1000);
    }

    // Random phone number like "0512345678"
    public static String generatePhone() {
        return "05" + (10000000 + random.nextInt(90000000));
    }
}
