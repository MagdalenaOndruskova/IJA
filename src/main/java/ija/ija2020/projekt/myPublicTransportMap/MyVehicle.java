/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt.myPublicTransportMap;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ija.ija2020.projekt.publicTransportMap.Drawable;
import ija.ija2020.projekt.publicTransportMap.MainController;
import ija.ija2020.projekt.publicTransportMap.TimeUpdate;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class handles all the functions for working with vehicle
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class MyVehicle implements Drawable, TimeUpdate{
    private String name;
    private MyLine line;
    private double speed;
    private List<String> time_schedule;
    @JsonIgnore
    private List<LocalTime> timeSchedule;
    @JsonIgnore
    private MyCoordinate position;
    @JsonIgnore
    private double distance =0;
    @JsonIgnore
    private MyPath path;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private double default_speed;
    @JsonIgnore
    private int timerStick =0;
    @JsonIgnore
    private boolean isOnRoute = false;
    @JsonIgnore
    private List<Shape> lineGui;
    @JsonIgnore
    private boolean isClicked = false;
    @JsonIgnore
    private List<MyStop> visitedStops = new ArrayList<>();

    /**
     * Empty constructor for reading data from YML
     */
    private MyVehicle() { }

    /**
     * Constructor for the class MyVehicle
     * @param name Some Id of the vehicle
     * @param line Line by which the vehicle is moving
     * @param speed Speed of the vehicle
     * @param time_schedule Time schedule in string format
     * @param timeSchedule Time schedule in LocalTime format
     */
    public MyVehicle(String name, MyLine line, double speed, List<String> time_schedule, List<LocalTime> timeSchedule) {
        this.name = name;
        this.line = line;
        this.speed = speed;
        this.time_schedule = time_schedule;
        this.timeSchedule = timeSchedule;
    }

    /**
     * Function returns vehicles path
     * @return Path of the vehicle
     */
    public MyPath getPath() {
        return path;
    }

    /**
     * Function returns actual position of vehicle
     * @return Position of the vehicle
     */
    public MyCoordinate getPosition() {
        return position;
    }

    /**
     * Function sets actual position of vehicle
     * @param position Actual position of vehicle
     */
    public void setPosition(MyCoordinate position) {
        this.position = position;
    }

    /**
     * Function returns vehicles line
     * @return Line of the vehicle
     */
    public MyLine getLine() {
        return line;
    }

    /**
     * Function sets line of the vehicle
     * @param line Line of the vehicle
     */
    public void setLine(MyLine line) {
        this.line = line;
    }

    /**
     * Function returns time schedule as list of time when vehicle is on some stop in string
     * @return List of Time schedule in string
     */
    public List<String> getTime_schedule() {
        return time_schedule;
    }

    /**
     * Function sets time schedule as list of strings
     * @param time_schedule List of time in string
     */
    public void setTime_schedule(List<String> time_schedule) {
        this.time_schedule = time_schedule;
    }

    /**
     * Function returns actual speed of vehicle
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Function sets speed of vehicle
     * @param speed Speed of vehicle
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Function transform time schedule in string to time schedule in Local Time
     * @param time_schedule time schedule in list of strings
     */
    @JsonIgnore
    public void setTimeSchedule(List<String> time_schedule) {
        timeSchedule = new ArrayList<>();
        for ( String time : time_schedule) {
            timeSchedule.add(LocalTime.parse(time));
        }
    }

    /**
     * Function returns time schedule as list of times
     * @return List of times at the stops
     */
    @JsonIgnore
    public List<LocalTime> getTimeSchedule() {
        return timeSchedule;
    }

    /**
     * Function sets start position of vehicle
     */
    @JsonIgnore
    public void set_startPosition()
    {
        position = line.getStops().get(0).getStreet().getStart();
    }

    /**
     * Function sets vehicle gui (circle colored with the same color as line )
     */
    public void setGui()
    {
        gui = new ArrayList<>();
        gui.add(new Circle(position.getX(), position.getY(), 5, Color.web(line.getColor())));
    }

    /**
     * Function returns vehicle name
     * @return name of the vehicle
     */
    public String getName() {
        return name;
    }

    /**
     * Function sets the name of vehicle
     * @param name Name of the vehicle
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Function returns list of object that represents the vehicle
     * @return List of object representing vehicle gui
     */
    @JsonIgnore
    @Override
    public List<Shape> getGui() {
        return gui;
    }

    /**
     * Function moves vehicle in the map
     * @param newCoor new coordinates of the vehicle
     */
    @JsonIgnore
    public void moveGui(MyCoordinate newCoor) {
        for (Shape shape : gui) {
            shape.setTranslateX((newCoor.getX() - position.getX()) + shape.getTranslateX());
            shape.setTranslateY((newCoor.getY() - position.getY()) + shape.getTranslateY());
        }

    }

    /**
     * Function returns the state, if vehicle is at the moment on the route
     * @return Boolean value if the vehicle is on the route
     */
    public boolean isOnRoute() {
        return isOnRoute;
    }

    /**
     * Function re-initialize list that saves which stops the vehicle visited
     */
    public void deleteVisitedStops()
    {
        visitedStops = new ArrayList<>();
    }

    /**
     * Main function that controlls updating position on the map when timer ticks
     * If the vehicle is currently clicked on, the timeschedule with stops is updated
     * If the distance is bigger than path length, the drawing stops and vehicle is removed from the map
     * @param timer Time in the timer
     * @param controller Main Controller from which is this function called
     */
    @Override
    public void update(LocalTime timer, MainController controller) {
        if (isClicked)
        {
            controller.drawLine(this, lineGui);
            isClicked = false;
        } else
            if ( controller.getVehicleLine() != null)
            {
                if (controller.getVehicleLine().equals(this))
                {
                    getLineGui();
                    controller.drawLine(this, lineGui);
                }
            }

        if (timer.equals(timeSchedule.get(0)))
        {
            if ( controller.getVehiclesInScene().contains(this))
                return;
            controller.getVehiclesInScene().add(this);
            controller.addElementToScene(this::getGui);
            isOnRoute = true;
        }
        else if (timer.isAfter(timeSchedule.get(0)))
        {
            distance += speed;
            double length = path.getPathSize();
            if (length < distance )
            {
                controller.deleteElementFromScene(this::getGui);
                controller.getVehiclesInScene().remove(this);
                return;
            }
            MyCoordinate newCoor = path.getNewCoordinates(distance);
            moveGui(newCoor);
            position = newCoor;
        }
    }

    /**
     * Function returns default speed (read from YML) of the vehicle
     * @return Default speed
     */
    public double getDefault_speed() {
        return default_speed;
    }

    /**
     * Function controls jumping in the time and counts the actual
     * position of the vehicle and which stops the vehicle visited
     * @param jumpedInTime Time to jump to
     */
    @Override
    public void jumpInTime(LocalTime jumpedInTime) {
        LocalTime jumpedTo = jumpedInTime;
        visitedStops = new ArrayList<>();
        while (jumpedInTime.isAfter(timeSchedule.get(0)))
        {
            distance += speed;
            position = path.getNewCoordinates(distance);
            jumpedInTime = jumpedInTime.plusSeconds(-30);
            for ( MyStop stop: path.getPathStops() ) {
                if ( ! visitedStops.contains(stop)) {
                    int index = path.getPathStops().indexOf(stop);
                    if (timeSchedule.get(index+1).isBefore(jumpedTo))
                    {
                        visitedStops.add(stop);
                        jumpedInTime = jumpedInTime.plusSeconds(-150);
                    }
                }
            }
        }
        setGui();
        isOnRoute = true;
        clickedOnVehicle();
    }

    /**
     * Function checks the traffic on the current street that vehicle is on
     * If the traffic is bigger than 0, then the vehicle is slowed down
     * Else the vehicle is set the default speed
     */
    @Override
    public void checkTraffic() {
        if (path.getCurrent_street().getTraffic() != 0)
        {
            speed = default_speed;
            slowDown(path.getCurrent_street().getTraffic());
        }
        else  speed= default_speed;
    }

    /**
     * Function that sets the timer stick for stopping on the stop
     * 5 means 5tics of timer, the vehicle wont move
     */
    @Override
    public void stopAtStop() {
        if (isAtStop())
        {
            if (timerStick == 0)
                timerStick = 5;
            else {
                timerStick -= 1;
                if (timerStick == 0)
                    speed = default_speed;
            }
        }
        if (timerStick != 0)
        {
            speed = 0;
        }

    }

    /**
     * Function returns true if the vehicle is at the stop or false if the not
     * @return  true if the vehicle is on the stop, false if it isnt
     */
    private boolean isAtStop() {
        for (MyStop stop : path.getCurrent_street().getStopsOnStreet()) {
            if ( ! path.getPathStops().contains(stop))
                continue;
            if (Math.abs(Math.abs(stop.getCoordinates().getX()) - Math.abs(position.getX()))<2)
            {
                if (Math.abs(Math.abs(stop.getCoordinates().getY()) - Math.abs(position.getY()))<2)
                {
                    visitedStops.add(stop);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Function slows down the vehicle
     * For each point of the traffic, the speed is reduced by 0.5point
     * The least possible speed of vehicle is 0.5
     * The bigger traffic, the more slower the vehicle
     * @param traffic Size of the traffic
     */
    private void slowDown(double traffic) {
        double vehicle_speed = speed;
        while ( traffic > 0)
        {
            vehicle_speed -= 0.5;
            if (vehicle_speed < 0.5)
            {
                vehicle_speed = 0.5; // najpomalsia rychlost
                break;
            }
            traffic -= 1;
        }
        speed = vehicle_speed;
    }

    /**
     * Function sets the distance of the vehicle
     * @param distance Distance that vehicle has driven
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Sets the action that is supposed to happend when user clicks on the vehicle
     */
    public void clickedOnVehicle()
    {
        gui.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            @FXML
            public void handle(MouseEvent event) {
                for ( MyStreet street: path.getPathStreets()) {
                    if (!street.getGui().get(0).getStroke().equals(Color.DARKRED))
                    {
                        street.getGui().get(0).setStroke(Color.web(line.getColor()));
                        street.getGui().get(0).setStrokeWidth(2.5);
                    }
                }
                getLineGui();
                isClicked = true;
            }
        });
    }

    /**
     * Function sets actual line gui the name of the stops, stops and the time
     * the vehicle was supposed to be there
     * If the vehicle already was at some stops, the circle is colored
     */
    public void getLineGui() {
        lineGui = new ArrayList<>();
        MyCoordinate coor = new MyCoordinate(30,20);
        int i = 0;
        int lineLength = 10;
        for ( MyStop stop: line.getStops()) {
            Circle stopCircle;
            if (visitedStops.contains(stop))
            {
                stopCircle = new Circle(180, coor.getY()+30, 10, Color.web(line.getColor()));
            }
            else stopCircle = new Circle(180, coor.getY()+30, 10, Color.TRANSPARENT );
            stopCircle.setStroke(Color.web(line.getColor()));
            stopCircle.setStrokeWidth(2);
            lineGui.add(stopCircle);

            Text stopName = new Text(50, coor.getY()+35, stop.getId());
            stopName.setStroke(Color.web(line.getColor()));
            lineGui.add(stopName);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            Text timeStop = new Text(220, coor.getY()+35, timeSchedule.get(i+1).format(formatter));
            timeStop.setStroke(Color.web(line.getColor()));
            lineGui.add(timeStop);
            i += 1;
            coor.setY(coor.getY()+50);
            lineLength += 50;
        }
        Line lineDraw = new Line(180,40,180,lineLength);
        lineDraw.setStroke(Color.web(line.getColor()));
        lineGui.add(lineDraw);
    }

    /**
     * Basic settings for the vehicle like start position, gui, path and default speed
     */
    public void settings()
    {
        set_startPosition();
        setGui();
        setTimeSchedule(time_schedule);
        path = new MyPath();
        path.setPath(line.getStops());
        clickedOnVehicle();
        default_speed = speed;
        path.findCurrentStreet(path.getPath().get(0), path.getPath().get(1));
    }


    /**
     * Function overrides the method toString for reading the values from YML
     * @return string constant
     */
    @Override
    public String toString() {
        return "MyVehicle{" +
                "name='" + name + '\'' +
                ", line=" + line +
                ", speed=" + speed +
                ", time_schedule=" + time_schedule +
                '}';
    }

    /**
     * Function returns true if the two vehicles are the same, or false if not
     * @param o Second object (vehicle) that is being compared
     * @return True if they are the same, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyVehicle myVehicle = (MyVehicle) o;
        return Double.compare(myVehicle.speed, speed) == 0 &&
                Double.compare(myVehicle.distance, distance) == 0 &&
                Double.compare(myVehicle.default_speed, default_speed) == 0 &&
                Objects.equals(name, myVehicle.name) &&
                Objects.equals(line, myVehicle.line) &&
                Objects.equals(timeSchedule, myVehicle.timeSchedule) &&
                Objects.equals(position, myVehicle.position) &&
                Objects.equals(path, myVehicle.path) &&
                Objects.equals(visitedStops, myVehicle.visitedStops);
    }

    /**
     * Conversion when overriding equals
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, line, speed, timeSchedule, position, distance, path, default_speed, visitedStops);
    }
}
