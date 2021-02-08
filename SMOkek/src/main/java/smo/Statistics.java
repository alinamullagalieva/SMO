package smo;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private final SetDispatcher setDispatcher;
    private final SelectionDispatcher selectionDispatcher;
    private final Buffer buffer;

    private List<SourceResult> sourceResultList;
    private List<DeviceResult> deviceResultList;

    private List<SourceResult> sourcesCurrState;
    private List<DeviceResult> devicesCurrState;
    private List<BufferResult> bufferCurrState;

    public Statistics(SetDispatcher setDispatcher, SelectionDispatcher selectionDispatcher, Buffer buffer) {
        this.setDispatcher = setDispatcher;
        this.selectionDispatcher = selectionDispatcher;
        this.buffer = buffer;
    }

    public List<SourceResult> getSourceResultList() {
        sourceResultList = new ArrayList<>(setDispatcher.getSources().size());
        for (int i = 0; i < setDispatcher.getSources().size(); ++i) {
            sourceResultList.add(new SourceResult());
            sourceResultList.get(i).setSourceNum(i);
            sourceResultList.get(i).setRequestNum(setDispatcher.getSources().get(i).getRequestNum() - 1);
            sourceResultList.get(i).setFailureProbability(buffer.getRejectedRequests()[i] * 1.0 / sourceResultList.get(i).getRequestNum());
            sourceResultList.get(i).setAverageWaitTime(selectionDispatcher.getWaitTime()[i] / sourceResultList.get(i).getRequestNum());
            sourceResultList.get(i).setAverageHandleTime(selectionDispatcher.getHandleTime()[i] / sourceResultList.get(i).getRequestNum());
            sourceResultList.get(i).setAverageTimeInSystem(sourceResultList.get(i).getAverageWaitTime() + sourceResultList.get(i).getAverageHandleTime());
        }
        return sourceResultList;
    }

    public List<DeviceResult> getDeviceResultList() {
        deviceResultList = new ArrayList<>(selectionDispatcher.getDevices().size());
        for (int i = 0; i < selectionDispatcher.getDevices().size(); ++i) {
            deviceResultList.add(new DeviceResult());
            deviceResultList.get(i).setDeviceNum(i);
            deviceResultList.get(i).setUtilization((selectionDispatcher.getDevices().get(i).getReleaseTime() -
                    selectionDispatcher.getDevices().get(i).getDowntime()) /
                    selectionDispatcher.getLastReleaseTime());
        }
        return deviceResultList;
    }

    public List<BufferResult> getBufferCurrState() {
        bufferCurrState = new ArrayList<>(buffer.getBufferSize());

        for (int i = 0; i < buffer.getBufferSize(); i++) {
            bufferCurrState.add(new BufferResult());
            bufferCurrState.get(i).setPosition(i);
            bufferCurrState.get(i).setGenTime(buffer.getBufferArr()[i].getGenerationTime());
            bufferCurrState.get(i).setSourceNum(buffer.getBufferArr()[i].getSourceId());
            bufferCurrState.get(i).setRequestNum(buffer.getBufferArr()[i].getRequestId());
        }
        return bufferCurrState;
    }

    public List<SourceResult> getSourcesCurrState() {
        sourcesCurrState = new ArrayList<>(setDispatcher.getSources().size());
        for (int i = 0; i < setDispatcher.getSources().size(); ++i) {
            sourcesCurrState.add(new SourceResult());
            sourcesCurrState.get(i).setSourceNum(i);
            sourcesCurrState.get(i).setGenTime(setDispatcher.getSources().get(i).getGenerationTime());
            sourcesCurrState.get(i).setReqNum(setDispatcher.getSources().get(i).getRequestNum());
            sourcesCurrState.get(i).setRejReqNum(buffer.getRejectedRequests()[i]);
        }
        return sourcesCurrState;
    }

    public List<DeviceResult> getDevicesCurrState() {
        devicesCurrState = new ArrayList<>(selectionDispatcher.getDevices().size());
        for (int i = 0; i < selectionDispatcher.getDevices().size(); ++i) {
            devicesCurrState.add(new DeviceResult());
            devicesCurrState.get(i).setDeviceNum(i);
            devicesCurrState.get(i).setReleaseTime(selectionDispatcher.getDevices().get(i).getReleaseTime());
            devicesCurrState.get(i).setDowntime(selectionDispatcher.getDevices().get(i).getDowntime());
            devicesCurrState.get(i).setSourceNum(selectionDispatcher.getDevices().get(i).getCurrentRequest() != null ?
                    selectionDispatcher.getDevices().get(i).getCurrentRequest().getSourceId() : 0);
            devicesCurrState.get(i).setRequestNum(selectionDispatcher.getDevices().get(i).getCurrentRequest() != null ?
                    selectionDispatcher.getDevices().get(i).getCurrentRequest().getRequestId() : 0);
            devicesCurrState.get(i).setGenTime(selectionDispatcher.getDevices().get(i).getCurrentRequest() != null ?
                    selectionDispatcher.getDevices().get(i).getCurrentRequest().getGenerationTime() : 0);
        }
        return devicesCurrState;
    }
}
