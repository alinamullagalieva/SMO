package controller;

import smo.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class MenuController {
    private App app;

    @FXML
    private TextField sNum;

    /*@FXML
    private TextField lambda;*/

    @FXML
    private TextField dNum;

    @FXML
    private TextField paramA;

    @FXML
    private TextField paramB;

    @FXML
    private TextField bSize;

    @FXML
    private TextField rNum;

    @FXML
    void initialize() {
        sNum.setText("10");
        dNum.setText("10");
        paramA.setText("1");
        paramB.setText("2.0");
        bSize.setText("5");
        rNum.setText("500");
    }

    void init() {
        int sourceNum = Integer.parseInt(sNum.getText());
        int deviceNum = Integer.parseInt(dNum.getText());
        double a = Double.parseDouble(paramA.getText());
        double b = Double.parseDouble(paramB.getText());
        int bufferSize = Integer.parseInt(bSize.getText());
        int requestsNum = Integer.parseInt(rNum.getText());
        app.init(new Config(sourceNum, /*flowIntensity,*/ deviceNum, a, b, bufferSize, requestsNum));
        app.runSimulation();
    }

    @FXML
    void startAutoMode(ActionEvent event) {
        init();
        app.openAutoMode();
    }

    @FXML
    void startStepMode(ActionEvent event) {
        init();
        app.openStepMode();
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setConfig(Config config){
        if (config != null) {
            sNum.setText(String.valueOf(config.getSourceNum()));
            //lambda.setText(String.valueOf(config.getFlowIntensity()));
            dNum.setText(String.valueOf(config.getDeviceNum()));
            paramA.setText(String.valueOf(config.getA()));
            paramB.setText(String.valueOf(config.getB()));
            bSize.setText(String.valueOf(config.getBufferSize()));
            rNum.setText(String.valueOf(config.getRequestsNum()));
        }
    }
}
