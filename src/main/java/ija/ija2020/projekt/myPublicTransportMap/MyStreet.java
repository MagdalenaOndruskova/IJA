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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import ija.ija2020.projekt.publicTransportMap.Drawable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents street in public transport map
 *
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
@JsonDeserialize(converter = MyStreet.StreetConstructor.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class MyStreet implements Drawable {
    private String name;
    private List<MyStop> stopsOnStreet;
    private MyCoordinate start, end;
    private boolean closed;
    private double traffic;

    @JsonIgnore
    private List<Shape> gui;

    /**
     * Empty constructor for deserializing YML
     */
    private MyStreet() { }

    /**
     * Constructor for street
     * @param name Name on the street
     * @param stopsOnStreet Stops that are on the street
     * @param start Coordinates that represent start of the street
     * @param end Coordinates that represenet end of the street
     * @param closed Variable that represent if the street is closed
     * @param traffic Variable that represent traffic on the street
     */
    public MyStreet(String name, List<MyStop> stopsOnStreet, MyCoordinate start, MyCoordinate end, boolean closed, double traffic) {
        this.name = name;
        this.stopsOnStreet = stopsOnStreet;
        this.start = start;
        this.end = end;
        this.closed = closed;
        this.traffic = traffic;
        setGui();
    }

    /**
     * Functions sets the street gui which is represented with:
     *       line - that represents the street
     *       Text - that represents the name of the street
     */
    public void setGui()
    {
        gui = new ArrayList<>();
        gui.add(new Line(this.start.getX(), this.start.getY(), this.end.getX(), this.end.getY()));
        gui.get(0).setStroke(Color.DARKSLATEBLUE);
        gui.get(0).setStrokeWidth(1.5);
    }

    /**
     * Function returns the street gui
     * @return List of shapes that are street gui
     */
    @JsonIgnore
    public List<Shape> getGui() {
        return this.gui;
    }

    /**
     * Function sets the start coordinates of the street
     * @param start Start coordinates of the street
     */
    public void setStart(MyCoordinate start) {
        this.start = start;
    }

    /**
     * Functions sets end coordinates of the street
     * @param end End coordinates of the street
     */
    public void setEnd(MyCoordinate end) {
        this.end = end;
    }

    /**
     * Function sets the name of street
     * @param name Name of the street
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Function returns the name of the street
     * @return Name of the street
     */
    public String getName() {
        return this.name;
    }

    /**
     * Function returns all the stops on the street
     * @return List of the stops on the street
     */
    public List<MyStop> getStopsOnStreet() {
        return stopsOnStreet;
    }

    /**
     * Function returns the state of the street - if its closed (true) or not (false)
     * @return True if the street is closed, false if not
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Function returns the value of traffic on the street.
     * The bigger value, the bigger traffic on the street.
     * @return Value representing traffic on the street
     */
    public double getTraffic() {
        return traffic;
    }

    /**
     * Function sets the stop on the street
     * @param stopsOnStreet list of stops on the street
     */
    public void setStopsOnStreet(List<MyStop> stopsOnStreet) {
        this.stopsOnStreet = stopsOnStreet;
    }

    /**
     * Function sets closedness of the street
     * @param closed Sets closed to this value
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * Sets the value of traffic to this value
     * @param traffic Number which holds how big is the traffic on the street
     */
    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    /**
     * Function returns start coordinates of the street
     * @return Start coordinate
     */
    public MyCoordinate getStart() {
        return start;
    }

    /**
     * Function returns end coordinates of the street
     * @return End coordinate
     */
    public MyCoordinate getEnd() {
        return end;
    }

    /**
     * Function overrides the method
     * @return String representation of street
     */
    @Override
    public String toString() {
        return "MyStreet{" +
                "name='" + name + '\'' +
                ", stopsOnStreet=" + stopsOnStreet +
                ", start=" + start +
                ", end=" + end +
                ", closed=" + closed +
                ", traffic=" + traffic +
                '}';
    }

    /**
     * Function sets the action which will be done when user clicks on the street
     *  (on the line representing it)
     *  One click with left button from the user gets the
     *      traffic one point higher and colors the street in Darkred color
     *  Clicked on the street with the right button will close the street
     */
    public void clickedOnStreet()
    {
        gui.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    traffic += 1;
                    gui.get(0).setStroke(Color.DARKRED);
                    gui.get(0).setStrokeWidth(5);
                } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                    closed = true;
                    gui.get(0).setStroke(Color.BLACK);
                    gui.get(0).setStrokeWidth(5);
                }
            }
        });
    }


    /**
     * Constructor which is called when user loads street from YML
     * Constructor sets gui of the street and action when user clicks on the street
     */
    static class StreetConstructor extends StdConverter<MyStreet, MyStreet>{
        @Override
        public MyStreet convert(MyStreet value) {
            value.setGui();
            value.clickedOnStreet();
            return value;
        }
    }
}
