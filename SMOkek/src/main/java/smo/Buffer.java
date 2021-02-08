package smo;

public class Buffer {
    private final int bufferSize;
    private int requestCount;
    private Request[] bufferArr;
    private final int[] rejectedRequests;

    private int prioritySource;

    public Buffer(int bufferSize, int sourceNum) {
        this.bufferSize = bufferSize;
        this.bufferArr = new Request[bufferSize];
        this.requestCount = 0;
        this.rejectedRequests = new int[sourceNum];

        for(int i = 0; i < bufferSize; i++){
            bufferArr[i] = new Request();
        }
        prioritySource = -1;
    }

    public boolean isEmpty() {
        for(int i = 0; i < bufferSize; i++){
            if((bufferArr[i].getRequestId() != -1) && (bufferArr[i].getSourceId() != -1)){
                return false;
            }
        }
        return true;
    }

    public boolean hasFreeSpaces() {
        return requestCount < bufferSize;
    }

    //постановка на свободное место
    public void putRequest(Request r) {
        int k = -1;

        for(int i = 0; i < bufferSize; i++){
            if(bufferArr[i].getRequestId() == -1){
                k = i;
                break;
            }
        }
        if(k != -1){
            bufferArr[k].setParameters(r);
            requestCount++;
        }
    }

    //отказ самой старой заявки
    public void replaceOldestRequest(Request r) {

        int k = -1;
        int minSourceId = 1000;
        double minGenTime = 1000000000;

        for(int i = 0;  i < bufferSize; i++){
            if(bufferArr[i].getSourceId() != -1){
                if( (bufferArr[i].getSourceId() <= minSourceId)
                        && (bufferArr[i].getGenerationTime() < minGenTime) ) {
                    minSourceId = bufferArr[i].getSourceId();
                    minGenTime = bufferArr[i].getGenerationTime();
                    k = i;
                }
                /*if(k != -1){
                    rejectedRequests[bufferArr[k].getSourceId()]++;
                    bufferArr[k].setParameters(r);
                }*/
            }
        }
        if(k != -1){
            rejectedRequests[bufferArr[k].getSourceId()]++;
            bufferArr[k].setParameters(r);
        }
    }

    Request getRequest() {
        if (prioritySource == -1){
            prioritySource = getPrioritySource();
        }

        Request r = new Request();

        if(isThereRequestsFromPrioritySource(prioritySource)){
            for(int i = 0; i < bufferSize; i++){
                if((bufferArr[i].getSourceId() != -1) && (bufferArr[i].getSourceId() == prioritySource)){
                    r.setParameters(bufferArr[i]);
                    bufferArr[i].removeRequest();
                    requestCount--;
                    break;
                }
            }
        } else{
            prioritySource = getPrioritySource();
            for(int i = 0; i < bufferSize; i++){
                if((bufferArr[i].getSourceId() != -1) && (bufferArr[i].getSourceId() == prioritySource)){
                    r.setParameters(bufferArr[i]);
                    bufferArr[i].removeRequest();
                    requestCount--;
                    break;
                }
            }
        }

        return r;
    }

    int getPrioritySource(){
        int s = -100;
        for(int i = 0; i < bufferSize; i++){
            if((bufferArr[i].getSourceId() != -1) && (bufferArr[i].getSourceId() > s)){
                s = bufferArr[i].getSourceId();
            }
        }
        return s;
    }

    boolean isThereRequestsFromPrioritySource(int s){
        for(int i = 0; i < bufferSize; i++){
            if((bufferArr[i].getSourceId() != -1) && (bufferArr[i].getSourceId() == s)){
                return true;
            }
        }
        return false;
    }

    public int[] getRejectedRequests() {
        return rejectedRequests;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public Request[] getBufferArr() {
        return bufferArr;
    }

}
