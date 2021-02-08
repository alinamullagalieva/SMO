package smo;

public class Source {
    private final int sourceNum;
    private final double a;
    private final double b;

    private int requestNum = 0;

    private double generationTime = 0.0;
    private Request request;

    public Source(int sourceNum, double a, double b) {
        this.sourceNum = sourceNum;
        this.a = a;
        this.b = b;
    }

    public void generateNewRequest() {
        ++requestNum;
        generationTime += (b - a) * Math.random() + a;
        request = new Request(generationTime, requestNum, sourceNum);
    }

    public double getGenerationTime() {
        return generationTime;
    }

    public Request getRequest() {
        return request;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public int getSourceNum() {
        return sourceNum;
    }
}
