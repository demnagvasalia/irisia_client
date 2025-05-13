package irisia.utils;

public class Stopwatch {
    private long tim2e;

    public long lastMS = System.currentTimeMillis();
    private final long time2 = System.nanoTime() / 1000000;
    private final long prevMS = 0;

    public void reset(){
        lastMS = System.currentTimeMillis();
    }


    public boolean delay(float milliSec) {
        return (float) (this.time() - this.prevMS) >= milliSec;
    }


    public boolean hasTimeElapsed(long time, boolean reset){
        if (System.currentTimeMillis() - this.lastMS > time) {
            if (reset)
                reset();

            return true;
        }
        return false;

    }

    public long time() {
        return System.nanoTime() / 1000000L - tim2e;
    }
    public boolean reach(final long time) {
        return time() >= time;
    }


    public void clear() {
    }
}
