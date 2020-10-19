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
 * Class represents the path of one vehicle
 *
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
public class MyPath {
    private List<MyCoordinate> path;
    private List<MyStreet> pathStreets;
    private List<MyStop> pathStops;
    MyStreet current_street;

    /**
     * Empty constructor for deserializing YML
     */
    public MyPath() {
    }

    /**
     * Constructor of path, which is represented with coordinates
     * @param path Path to be constructed
     */
    public MyPath(List<MyCoordinate> path) {
        this.path = path;
    }

    /**
     * Function returns current street on which is the vehicle
     * @return Street on which is the vehicle at the moment
     */
    public MyStreet getCurrent_street() {
        return current_street;
    }

    /**
     * Function sets current street
     * @param current_street Street on which vehicle is at the moment
     */
    public void setCurrent_street(MyStreet current_street) {
        this.current_street = current_street;
    }

    /**
     * Function returns all the streets through which the vehicle will go
     * @return List of streets
     */
    public List<MyStreet> getPathStreets() {
        return pathStreets;
    }

    /**
     * Function returns all the path stops, on which the vehicle will also stop for some time
     * @return List of stops
     */
    public List<MyStop> getPathStops() {
        return pathStops;
    }

    /**
     * Function returns the path which is list of coordinates
     * @return Path
     */
    public List<MyCoordinate> getPath() {
        return path;
    }

    /**
     * Function sets the path by coordinates from the list of stops (gotten from the line)
     * @param stops List of stops of the line
     */
    public void setPath(List<MyStop> stops) {
        path = new ArrayList<>();
        for (int i = 0; i < stops.size()-1; i++)
        {
            MyCoordinate coor1_start = stops.get(i).getStreet().getStart();
            MyCoordinate coor1_end = stops.get(i).getStreet().getEnd();
            MyCoordinate coor2_start = stops.get(i+1).getStreet().getStart();
            MyCoordinate coor2_end = stops.get(i+1).getStreet().getEnd();
            if ((coor1_end.equals(coor2_end) || coor1_end.equals(coor2_start)) &&
                    (coor1_start.equals(coor2_end) || coor1_start.equals(coor2_start)))
                continue;
            if (coor1_end.equals(coor2_start) || coor1_end.equals(coor2_end))
            {
                if ( path.size() == 0)
                    path.add(coor1_start);
                path.add(coor1_end);
            }
            else if (coor1_start.equals(coor2_start) || coor1_start.equals(coor2_end))
            {
                if (path.size() ==0)
                    path.add(coor1_end);
                path.add(coor1_start);
            }
        }
        MyStop lastStop = stops.get(stops.size()-1);
        MyStreet lastStreet = lastStop.getStreet();
        MyCoordinate lastCoordinate = path.get(path.size()-1);
        if (lastCoordinate.equals(lastStreet.getStart()))
        {
            path.add(lastStreet.getEnd());
        }
        else path.add(lastStreet.getStart());
        setPathStopsStreets(stops);
    }

    /**
     * Sets the stops of the path
     * @param stops list of stops
     */
    public void setPathStopsStreets(List<MyStop> stops)
    {
        pathStops = new ArrayList<>();
        pathStreets = new ArrayList<>();
        for ( MyStop stop : stops ) {
            pathStops.add(stop);
            pathStreets.add(stop.getStreet());
        }
    }

    /**
     * Function counts the whole size of the path
     * @return size of the path
     */
    public double getPathSize()
    {
        double size = 0;
        for (int i = 0; i < path.size()-1; i++)
        {
            size += path.get(i).countVector(path.get(i+1));
        }
        return size;
    }

    /**
     * Counts the new coordinates by the distance the vehicle has passed
     * In this function is also set the current street
     * @param distance Distance the vehicle has passed through
     * @return New position (coordinates) of the vehicle
     */
    public MyCoordinate getNewCoordinates(double distance)
    {
        double length = 0;
        MyCoordinate coordinate1 = null;
        MyCoordinate coordinate2 = null;
        for (int i=0; i<path.size()-1; i++)
        {
            coordinate1 =path.get(i);
            coordinate2 = path.get(i+1);

            if (length + coordinate1.countVector(coordinate2) > distance)
                break;
            length += coordinate1.countVector(coordinate2);
        }
        if (coordinate1 == null ||coordinate2 == null)
            return null;

        findCurrentStreet(coordinate1,coordinate2);
        double driven = (distance-length) / coordinate1.countVector( coordinate2);
        return new MyCoordinate(coordinate1.getX() + (coordinate2.getX() - coordinate1.getX())*driven,
                coordinate1.getY() + (coordinate2.getY() - coordinate1.getY())*driven);

    }

    /**
     * Function finds the current street by given coordinates
     * Coordinates are the start and the end of some street
     * @param coor1 Coordinates of the one side of the street
     * @param coor2 Coordinates of the other side of the street
     */
    public void findCurrentStreet(MyCoordinate coor1, MyCoordinate coor2)
    {
        for ( MyStreet street : pathStreets ) {
            MyCoordinate street_start = street.getStart();
            MyCoordinate street_end = street.getEnd();
            if ((street_start.equals(coor1) || street_start.equals(coor2))
                    && (street_end.equals(coor1) || street_end.equals(coor2)))
            {
                current_street = street;
                break;
            }

        }
    }
}
