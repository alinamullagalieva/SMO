package smo;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import controller.MenuController;
import controller.AutoModeController;
import controller.StepModeController;
import javafx.application.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private Stage primaryStage;

    private MenuController menuController;
    private AutoModeController autoModeController;
    private StepModeController stepModeController;

    private int currentRequestsNum;
    private int requestsNum;
    private SetDispatcher setDispatcher;
    private SelectionDispatcher selectionDispatcher;
    private Buffer buffer;
    private Statistics statistics;

    private List<String> eventCalendar;
    private List<List<SourceResult>> sourcesStates;
    private List<List<BufferResult>> bufferStates;
    private List<List<DeviceResult>> devicesStates;

    private Config config;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        openMenu();
        primaryStage.show();
        config = null;
    }

    public void init(Config config) {
        this.config = config;
        ArrayList<Source> sources = new ArrayList<>(config.getSourceNum());
        for (int i = 0; i < config.getSourceNum(); i++) {
            sources.add(new Source(i, config.getA(), config.getB()));
        }

        ArrayList<Device> devices = new ArrayList<>(config.getDeviceNum());
        for (int i = 0; i < config.getDeviceNum(); i++) {
            devices.add(new Device(i));
        }

        buffer = new Buffer(config.getBufferSize(), config.getSourceNum());

        selectionDispatcher = new SelectionDispatcher(buffer, devices, config.getSourceNum());
        setDispatcher = new SetDispatcher(buffer, sources, selectionDispatcher);

        setDispatcher.initSources();
        currentRequestsNum = 0;
        requestsNum = config.getRequestsNum();
        statistics = new Statistics(setDispatcher, selectionDispatcher, buffer);

        eventCalendar = new ArrayList<>();
        sourcesStates = new ArrayList<>();
        bufferStates = new ArrayList<>();
        devicesStates = new ArrayList<>();
    }

    public void runSimulation() {
        while (currentRequestsNum < requestsNum ) {
            setDispatcher.setCurrentSource();
            selectionDispatcher.setCurrentDevice();

            if (!buffer.isEmpty() && (selectionDispatcher.getCurrentDevice().getReleaseTime() <
                    setDispatcher.getCurrentSource().getGenerationTime())) {
                selectionDispatcher.getRequestFromBuffer();
                selectionDispatcher.sendRequestToDevice();
                eventCalendar.add(selectionDispatcher.getCurrentDevice().getDeviceNum()+" прибор свободен.\n" +
                        "Взяли заявку из буфера.\n" +
                        "Отправили обрабатываться на прибор.\n" +
                        "------------");
            } else {
                setDispatcher.getRequest();
                setDispatcher.sendRequest(eventCalendar);
                setDispatcher.generateNewRequest();
                currentRequestsNum++;
            }
            sourcesStates.add(statistics.getSourcesCurrState());
            bufferStates.add(statistics.getBufferCurrState());
            devicesStates.add(statistics.getDevicesCurrState());
        }
        while (!buffer.isEmpty()) {
            selectionDispatcher.setCurrentDevice();
            selectionDispatcher.getRequestFromBuffer();
            selectionDispatcher.sendRequestToDevice();
            eventCalendar.add(selectionDispatcher.getCurrentDevice().getDeviceNum()+" прибор свободен.\n" +
                    "Взяли заявку из буфера.\n" +
                    "Отправили обрабатываться на прибор.\n" +
                    "------------");
            sourcesStates.add(statistics.getSourcesCurrState());
            bufferStates.add(statistics.getBufferCurrState());
            devicesStates.add(statistics.getDevicesCurrState());
        }
    }

    public void openMenu() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        try {
            primaryStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Menu");
        MenuController controller = loader.getController();
        this.menuController = controller;
        controller.setApp(this);
        menuController.setConfig(config);
    }

    public void openAutoMode() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/autoMode.fxml"));
        try {
            primaryStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Auto Mode");
        this.autoModeController = loader.getController();
        primaryStage.show();
        autoModeController.setApp(this);
        autoModeController.printInfo(statistics);
    }

    public void openStepMode() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/stepMode.fxml"));
        try {
            primaryStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Step Mode");
        this.stepModeController = loader.getController();
        primaryStage.show();
        stepModeController.setApp(this);
        stepModeController.printInfo(eventCalendar, sourcesStates, bufferStates, devicesStates);
    }
}
