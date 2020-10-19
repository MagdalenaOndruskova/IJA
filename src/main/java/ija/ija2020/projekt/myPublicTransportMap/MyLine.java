/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt.myPublicTransportMap;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

/**
 * Class represents one line of public transport
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class MyLine  {
    private String name;
    private Integer id;
    private String color;
    private List<MyStop> stops;

    /**
     * Empty constructor for deserializing YML
     */
    private MyLine() {
    }

    /**
     * Class constructor specifieng some of the basic values
     * @param name Name of the Line
     * @param id Number of the Line
     * @param color Color in which the vehicles/streets will be colored if they belong to this class
     * @param stops List of stops that are parts of this line
     */
    public MyLine(String name, Integer id, String color, List<MyStop> stops) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.stops = stops;
    }

    /**
     * Function returns the color of line
     * @return Color of the line
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of line
     * @param color Color of line
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Function returns name of the line
     * @return name of the line
     */
    public String getName() {
        return name;
    }

    /**
     * Function sets the name of line
     * @param name Name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Function returns ID of the line
     * @return ID of the line
     */
    public Integer getId() {
        return id;
    }

    /**
     * Function sets ID of the line
     * @param id ID to be set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Function returns list of stops of the line
     * @return List of Stops
     */
    public List<MyStop> getStops() {
        return stops;
    }

    /**
     * Function sets the stops of the line
     * @param stops stops where the line is stopping
     */
    public void setStops(List<MyStop> stops) {
        this.stops = stops;
    }

    /**
     * Function overrides the method toString
     * @return String constant
     */
    @Override
    public String toString() {
        return "MyLine{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", color='" + color + '\'' +
                ", stops=" + stops +
                '}';
    }
}
