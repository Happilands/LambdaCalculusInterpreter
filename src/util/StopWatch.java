package util;

public class StopWatch {
    private final long start;

    public StopWatch(){
        start = System.currentTimeMillis();
    }

    public long getMillis(){
        return System.currentTimeMillis() - start;
    }

    public void printTime(){
        System.out.printf("Time: %dms\n", getMillis());
    }
}
