package gateway;

/**
 * A rate limiter, can be used to restrict the rate at which some resource is accessed. For my purposes, this class is
 * used to limit the number of API requests in a given time period.
 *
 * @author Peter Loomis
 */

public class RateLimiter {

    private static final double RATE=5.0; // number of requests
    private static final double PERIOD=2.0; // a max of RATE requests can be carried out in this amount of time (seconds)
    private double allowance; // used to determine when the rate limit has been exceeded
    private double last_check; // time at which the last request was processed

    public RateLimiter() {
        allowance = RATE;
        last_check = (double)System.currentTimeMillis()/1000;
    }

    /**
     * Implements the token bucket algorithm to control the number of requests processed in a certain amount of time.
     * The allowance variable is used as the bucket. The allowance variable is decremented every time a request is
     * processed. If the value of allowance becomes less than one, then the bucket is empty, which means the rate limit
     * has been exceeded.
     *
     * @return boolean value indicating whether the rate limit has been exceeded
     */
    public boolean limitExceeded() {
        double current = (double)System.currentTimeMillis()/1000;
        double time_passed = current - last_check;
        last_check = current;
        allowance += time_passed * (RATE/PERIOD);
        if (allowance > RATE) {
            allowance = RATE;
        }
        if (allowance < 1.0) { // the rate limit has been exceeded
            return true;
        }
        allowance -= 1.0;
        return false;
    }
}
