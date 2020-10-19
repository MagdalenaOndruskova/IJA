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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents stop in the public transport map
 *
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
@JsonDeserialize(converter = MyStop.StopConstructor.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MyStop implements Drawable {
    private String id;
    private MyCoordinate coordinates;
    @JsonIgnore
    private List<Shape> gui;
    @JsonIgnore
    private MyStreet street;

    /**
     * Empty constructor for deserializing YML
     */
    public MyStop() {
    }

    /**
     * Constructor sets the coordinates and id of the stop
     * @param id Stop id
     * @param coordinates Stop coordinates
     */
    public MyStop(String id, MyCoordinate coordinates) {
        this.id = id;
        this.coordinates = coordinates;
        setGui();
    }

    /**
     * Functions sets the street on which this stop is on
     * @param map Public Transport Map with all streets
     */
    public void setStreet(MyMap map)
    {
        List<MyStreet> streets = map.getStreets();
        for (MyStreet street : streets)
        {
            for (MyStop stop : street.getStopsOnStreet())
            {
                if (stop == this )
                    this.street = street;
            }
        }
    }

    /**
     * Function returns the street on which is Stop located
     * @return Stops street
     */
    public MyStreet getStreet()
    {
        return street;
    }

    /**
     * Function returns stop Id
     * @return Stop Id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets stops Id
     * @param id Id of stop
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Function returns stop coordinates
     * @return Coordinates of the stop
     */
    public MyCoordinate getCoordinates() {
        return coordinates;
    }

    /**
     * Function sets stop coordinates
     * @param coordinates Coordinates on which the stop is located
     */
    public void setCoordinates(MyCoordinate coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Function sets Stop gui. Gui has two parts:
     *          Cirle that represents the stop
     *          Text that holds the stop ID
     */
    public void setGui()
    {
        gui = new ArrayList<>();
        gui.add(new Circle(coordinates.getX(), coordinates.getY(), 6.5, Color.LIGHTGREEN ));
    }

    /**
     * Function overrides method to hold the string representation
     * @return string constant representing stop
     */
    @Override
    public String toString() {
        return "MyStop{" +
                "id='" + id + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }

    /**
     * Function returns stops gui
     * @return List of shapes that represents stop gui
     */
    @JsonIgnore
    @Override
    public List<Shape> getGui() {
        return gui;
    }

    /**
     * Constructor that is called when deserializing stop from YML
     */
    static class StopConstructor extends StdConverter<MyStop, MyStop>
    {
        @Override
        public MyStop convert(MyStop value) {
            value.setGui();
            return value;
        }
    }
}
