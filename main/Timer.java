package main;

public class Timer
{
    private long starttime;

    public void start()
    {
        starttime = System.nanoTime();
    }

    public double getNanoseconds()
    {
        return (double)(System.nanoTime() - starttime);
    }

    public double getMicroseconds()
    {
        return getNanoseconds() / 1000;
    }

    public double getMilliseconds()
    {
        return getMicroseconds() / 1000;
    }

    public double getSeconds()
    {
        return getMilliseconds() / 1000;
    }
}
