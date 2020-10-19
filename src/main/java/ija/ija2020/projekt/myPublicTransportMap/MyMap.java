/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt.myPublicTransportMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Class holds the information of all the streets, stops, lines and vehicles of public transport map
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
public class MyMap {

    private List<MyStreet> streets;
    private List<MyStop> stops;
    private List<MyLine> lines;
    private List<MyVehicle> vehicles;

    /**
     * Constructor for the lists that holds the information
     */
    public MyMap() {
        streets = new ArrayList<>();
        stops = new ArrayList<>();
        lines = new ArrayList<>();
        vehicles = new ArrayList<>();
    }

    /**
     * Functions returns list of vehicles that (at some time) will be in the map
     * @return List of vehicles
     */
    public List<MyVehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Sets the list of vehicles to this class
     * @param vehicles list of vehicles
     */
    public void setVehicles(List<MyVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    /**
     * Sets the list of streets to this class
     * @param streets list of streets
     */
    public void setStreets(List<MyStreet> streets) {
        this.streets = streets;
    }

    /**
     * Sets the list of stops to this class
     * @param stops list of stops
     */
    public void setStops(List<MyStop> stops) {
        this.stops = stops;
    }

    /**
     * Sets the list of lines to this class
     * @param lines list of lines
     */
    public void setLines(List<MyLine> lines) {
        this.lines = lines;
    }

    /**
     * Function returns list of streets in the map
     * @return List of streets
     */
    public List<MyStreet> getStreets() {
        return streets;
    }

    /**
     * Function returns list of stops in the map
     * @return List of stops
     */
    public List<MyStop> getStops() {
        return stops;
    }

    /**
     * Function returns list of lines in the map
     * @return List of lines
     */
    public List<MyLine> getLines() {
        return lines;
    }

    /**
     * Function overrides the method toString
     * @return string that represents this class
     */
    @Override
    public String toString() {
        return "MyMap{" +
                "streets=" + streets +
                ", stops=" + stops +
                ", lines=" + lines +
                ", vehicles=" + vehicles +
                '}';
    }

}
