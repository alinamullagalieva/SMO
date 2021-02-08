package smo;

public class Request {
    private /*final*/ double generationTime;
    private double waitTime = 0;
    private double handleTime;
    private /*final*/ int requestId;
    private /*final*/ int requestSourceId;

    public Request(double generationTime, int requestNumber, int sourceNumber) {
        this.generationTime = generationTime;
        this.requestId = requestNumber;
        this.requestSourceId = sourceNumber;
    }

    public Request(){
        this.generationTime = 0;
        this.requestId = -1;
        this.requestSourceId = -1;
    }

    public void removeRequest() {
        this.generationTime = 0;
        this.requestId = -1;
        this.requestSourceId = -1;
    }

    public void setParameters(Request r){
        this.requestId = r.getRequestId();
        this.requestSourceId = r.getSourceId();
        this.generationTime = r.getGenerationTime();
    }

    public double getGenerationTime() {
        return generationTime;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getSourceId() {
        return requestSourceId;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public double getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(double handleTime) {
        this.handleTime = handleTime;
    }
}
