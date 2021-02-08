package smo;// равномерный закон распределения времени обслуживания

public class Device {
    private final int deviceNum;
    private double startTime = 0;
    private double releaseTime = 0;
    private Request currentRequest = null;

    private double downtime = 0;

    public Device(int deviceNum) {
        this.deviceNum = deviceNum;
    }

    public Request handleRequest(Request request) {
        this.currentRequest = request;
        if (request.getGenerationTime() > startTime) {
            downtime += request.getGenerationTime() - startTime;
            startTime = request.getGenerationTime();
        } else {
            request.setWaitTime(startTime - request.getGenerationTime());
        }

        double processTime = Math.exp(Math.random()); // время обработки одной заявки
        request.setHandleTime(processTime);
        releaseTime = startTime + processTime;
        startTime = releaseTime;

        return request;
    }

    public double getReleaseTime() {
        return releaseTime;
    }

    public double getDowntime() {
        return downtime;
    }

    public int getDeviceNum() {
        return deviceNum;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }
}
