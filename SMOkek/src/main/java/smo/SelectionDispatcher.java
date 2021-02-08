package smo;

import java.util.ArrayList;

public class SelectionDispatcher {
    private final Buffer buffer;
    private final ArrayList<Device> devices;

    private Device currentDevice;
    private Request currentRequest;

    private final double[] waitTime;
    private final double[] handleTime;

    private int lastDevice;

    public SelectionDispatcher(Buffer buffer, ArrayList<Device> devices, int sourceNum) {
        this.buffer = buffer;
        this.devices = devices;
        this.waitTime = new double[sourceNum];
        this.handleTime = new double[sourceNum];

        lastDevice = 0;
    }

    public boolean hasFreeDevice(double currentTime) {
        for (Device device : devices) {
            if (device.getReleaseTime() < currentTime) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentDevice() {

        int number = (lastDevice == (devices.size()/* - 1*/)) ? 0 : (lastDevice);
        for(int i = 0; i < devices.size(); i++){
            if(devices.get(number).getReleaseTime() < devices.get(lastDevice).getReleaseTime()){
                lastDevice = number;
                break;
            } else{
                number++;
                number = (number == (devices.size()/* - 1*/)) ? 0 : number;
            }
        }

        currentDevice = devices.get(lastDevice);
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void getRequestFromSource(Request r) {
        currentRequest = r;
    }

    public void getRequestFromBuffer() {
        currentRequest = buffer.getRequest();
    }

    public void sendRequestToDevice() {
        Request request = currentDevice.handleRequest(currentRequest);
        waitTime[request.getSourceId()] += request.getWaitTime();
        handleTime[request.getSourceId()] += request.getHandleTime();
    }

    public double getLastReleaseTime() {
        double time = devices.get(0).getReleaseTime();
        for (Device device : devices) {
            if (device.getReleaseTime() > time) {
                time = device.getReleaseTime();
            }
        }

        return time;
    }

    public double[] getWaitTime() {
        return waitTime;
    }

    public double[] getHandleTime() {
        return handleTime;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }
}
