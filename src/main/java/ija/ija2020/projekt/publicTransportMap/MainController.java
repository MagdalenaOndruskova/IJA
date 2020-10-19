/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt.publicTransportMap;

import ija.ija2020.projekt.myPublicTransportMap.MyMap;
import ija.ija2020.projekt.myPublicTransportMap.MyStreet;
import ija.ija2020.projekt.myPublicTransportMap.MyVehicle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that represents the gui and controls all the action with it
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
public class MainController {
    @FXML
    private Pane map_base;
    @FXML
    private TextField timeScale;
    @FXML
    private Pane line_base;
    @FXML
    private Label time_label;
    @FXML
    private TextField timeChange;

    private List<Drawable> elements = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.of(7,59,30);
    private LocalTime maxTime = LocalTime.of(23,00,00);
    private List<TimeUpdate> updates = new ArrayList<>();
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private List<MyVehicle> vehicles = new ArrayList<>();
    private MyMap map;
    private MyVehicle vehicleLine = null;


    /**
     * Reads the value from text field and starts the timer X times faster/slower
     * If the value is bigger than 55 or smaller than 0 the alert is shown
     * If the value is not a double number, alert is shown.
     */
    @FXML
    private void onTimeScaleChange()
    {
        try {
            float scale = Float.parseFloat(timeScale.getText());
            if ( scale <=0 )
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid timescale");
                alert.showAndWait();
            }
            if ( scale > 55 )
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid timescale - too big");
                alert.showAndWait();
            }
            timer.cancel();
            starTime(scale, this);         }
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid timescale");
            alert.showAndWait();
        }
    }

    /**
     * Gets the map that has all the streets, stops, vehicles and lines in it
     * @return Public transport map
     */
    public MyMap getMap() {
        return map;
    }

    /**
     * Sets the public transport map to the controller
     * @param map Map to be set
     */
    public void setMap(MyMap map) {
        this.map = map;
    }

    /**
     * Function returns all the vehicles that are currently present on the map
     * @return list of vehicles
     */
    public List<MyVehicle> getVehiclesInScene() {
        return vehicles;
    }

    /**
     * Function resets the traffic to the original traffic (normal traffic stiuation)
     */
    @FXML
    private void onResetTraffic()
    {
        for (MyStreet street: map.getStreets()) {
            if (street.getGui().get(0).getStroke().equals(Color.DARKRED))
            {
                street.getGui().get(0).setStroke(Color.DARKSLATEBLUE);
                street.getGui().get(0).setStrokeWidth(1.5);
                street.setTraffic(0);
            }
        }
    }

    /**
     * Function resets colored lines and shown line to the default color
     */
    @FXML
    private void onResetLines()
    {
        deleteLine();
        vehicleLine = null;
        for (MyStreet street: map.getStreets())
        {
            if ( !street.getGui().get(0).getStroke().equals(Color.DARKRED))
            {
                street.getGui().get(0).setStroke(Color.DARKSLATEBLUE);
                street.getGui().get(0).setStrokeWidth(1.5);
                street.setTraffic(0);
            }
        }
    }

    /**
     * Function resets the time to the default (start) time
     */
    @FXML
    private void onResetTime()
    {
        deleteLine();
        vehicleLine = null;
        for (MyVehicle vehicle:vehicles) {
            vehicle.setDistance(0);
            map_base.getChildren().removeAll(vehicle.getGui());
            vehicle.setSpeed(vehicle.getDefault_speed());
            vehicle.settings();
            vehicle.deleteVisitedStops();
        }
        for (TimeUpdate update : updates)
        {
            if (update instanceof  MyVehicle)
            {
                ((MyVehicle) update).setDistance(0);
                ((MyVehicle) update).settings();
            }
        }
        vehicles = new ArrayList<>();
        time = LocalTime.of(7,59,30);
        timer.cancel();
        starTime(1, this);
    }

    /**
     * Function resets detours - the color in GUI and also opens the street
     */
    @FXML
    private void onResetDetours()
    {
        for (MyStreet street: map.getStreets())
        {
            if ( street.getGui().get(0).getStroke().equals(Color.BLACK))
            {
                street.getGui().get(0).setStroke(Color.DARKSLATEBLUE);
                street.getGui().get(0).setStrokeWidth(1.5);
                street.setClosed(false);
            }
        }
    }

    /**
     * Function controls jump in the time - function deletes all vehicles on the scene
     * and start finding vehicles that should be on the new time on the line
     */
    @FXML
    private void onJumpInTime()
    {
        onResetTime();
        LocalTime timeField = LocalTime.parse(timeChange.getText());
        time = timeField;
        for ( TimeUpdate update : updates ) {
            if (update instanceof  MyVehicle)
            {
                LocalTime vehicleStart= ((MyVehicle) update).getTimeSchedule().get(0);
                LocalTime vehicleEnd = ((MyVehicle) update).getTimeSchedule().get(((MyVehicle) update).getTimeSchedule().size()-1);
                if ( (vehicleStart.isBefore(timeField) || vehicleStart.equals(timeField))&&
                        (vehicleEnd.isAfter(timeField) || vehicleEnd.equals(timeField)))
                {
                    vehicles.add((MyVehicle) update);
                    ((MyVehicle) update).setDistance(0);
                    ((MyVehicle) update).settings();
                    update.jumpInTime(timeField);
                    map_base.getChildren().addAll(((MyVehicle) update).getGui());
                }
            }
        }
    }

    /**
     * Function handles zooming of the scene
     * @param scrolling determinates if the scene is getting "bigger" or smaller
     */
    @FXML
    private void doZoom(ScrollEvent scrolling)
    {
        scrolling.consume();
        double zoom;
        if (scrolling.getDeltaY() > 1) zoom = 1.1;
        else zoom = 0.9;
        map_base.setScaleX(zoom * map_base.getScaleX());
        map_base.setScaleY(zoom * map_base.getScaleY());
        map_base.layout();
    }

    public MyVehicle getVehicleLine() {
        return vehicleLine;
    }

    public void setVehicleLine(MyVehicle vehicleLine) {
        this.vehicleLine = vehicleLine;
    }

    /**
     * Function draws on the left side of aplication current line of the vehicle
     * @param vehicle vehicle that was clicked
     * @param elements List of all elements (line, circles and text) to be drawn
     */
    public void drawLine(MyVehicle vehicle, List<Shape> elements){
        deleteLine();
        vehicleLine = vehicle;
        for ( Shape element: elements) {
            line_base.getChildren().addAll(element);
        }
    }

    /**
     * Function deletes the line shown on the left side of the application
     */
    public void deleteLine()
    {
        line_base.getChildren().removeAll(line_base.getChildren());
    }

    /**
     * Function sets elements that will be updating in the time
     * @param element Element that will be updated everytime timer ticks
     */
    public void setItemsUpdatingInTime(Drawable element)
    {
        if (element instanceof  TimeUpdate)
            updates.add((TimeUpdate) element);
    }

    /**
     * Function adds element to the scene
     * @param element Element to be added in the scene
     */
    public void addElementToScene(Drawable element)
    {
        map_base.getChildren().addAll(element.getGui());
    }

    /**
     * Function delete element from the scene
     * @param element Element to be deleted
     */
    public void deleteElementFromScene(Drawable element)
    {
        map_base.getChildren().removeAll(element.getGui());
    }

    /**
     * Function sets elements to the scene
     * @param elements List of elements
     */
    public void setElements(List<Drawable> elements) {
        map_base.getChildren().removeAll(map_base.getChildren());
        this.elements = elements;
        for (Drawable draw : elements) {
            addElementToScene(draw);
            setItemsUpdatingInTime(draw);
        }
    }


    /**
     * Function show actual time in the label in the format HH:mm:ss
     * @param time actual time to be shown
     */
    @FXML
    public void setLabel (LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        time_label.setText(time.format(formatter));
    }

    /**
     * Function starts the time and every seconds adds another 30seconds to the time simulated in the program
     * If the time is after 23:00:00 the time stops and starts again at 08:00:00
     * @param scale How fast am I adding the 30seconds to the time
     * @param controller This class that is holding all the information I need
     */
    public void starTime(float scale, MainController controller)
    {
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{setLabel(time);});

                for (TimeUpdate update : updates)
                {
                    Platform.runLater(()->{
                        update.update(time, controller );
                        if ( update instanceof MyVehicle)
                        {
                            if (((MyVehicle) update).isOnRoute())
                            {
                                Platform.runLater(()->
                                {
                                    update.checkTraffic();
                                    update.stopAtStop();
                                });
                            }
                        }
                    });
                }
                time = time.plusSeconds(30); // každú sekundu tomu pridá 30sekund
                if (time.isAfter(maxTime))
                    Platform.runLater(()->{
                        onResetTime();
                    });

            }
        }, 0, (long) (1000 / scale));

    }
}
