package com.mycompany.serialgraph;

import com.mycompany.serialgraph.model.SimpleSerialPort;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;

public class FXMLController implements Initializable {
    
    private SimpleSerialPort serialPort;
    
    @FXML
    private TextArea messageLog;
    
    @FXML
    private ComboBox<String> portSelector;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private LineChart<Number, Number> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    
    
    @FXML
    void clearMessageLog(ActionEvent event) {
        messageLog.setText("");
    }
    
    @FXML
    void onPortSelected(ActionEvent event) {
        try {
            String portName = portSelector.getValue();
            serialPort = new SimpleSerialPort(portName);
            serialPort.subscribe((Character data) -> {
                Platform.runLater(()->{
                    messageLog.appendText(data.toString());
                });     
            });
            addOneExitListener();
            setDisabled(false);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void addOneExitListener(){
        messageLog.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.runLater(() -> {
                    serialPort.close();
                });
            }
        });
    }

    void appendToLog(String newLine){
        messageLog.appendText(newLine);
    }
    
    void setDisabled(boolean state){
        clearButton.setDisable(state);
    }
    
    private int counter;
    private XYChart.Series series;
    
    void plotSomething(){
        series = new XYChart.Series();
        series.setName("X^2");
        for (counter = 0; counter < 20; counter++) {
            series.getData().add(new XYChart.Data(counter,counter*counter));
        }
        chart.getData().add(series);
    }
    
    @FXML
    void doSomething(ActionEvent event) {
        messageLog.setText("hello world");
        series.getData().add(new XYChart.Data(counter,counter*counter));
        counter++;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] ports = SimpleSerialPort.getPorts();
        ObservableList<String> options = FXCollections.observableArrayList(ports);
        portSelector.setItems(options);
        setDisabled(true);
        plotSomething();
    }    
}
