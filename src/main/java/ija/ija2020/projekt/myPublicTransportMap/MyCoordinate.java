/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt.myPublicTransportMap;

import java.util.Objects;

/**
 * Class implements coordinates in the map
 * @version  1.0
 * @author Magdaléna Ondrušková
 */
public class MyCoordinate{
    private double x;
    private double y;

    /**
     * Empty constructor for deserializing YML
     */
    private MyCoordinate(){}

    /**
     * Constructor of coordinate
     * @param x value of X part of coordinate
     * @param y value of Y part of coordinate
     */
    public MyCoordinate(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets points X coordinate
     * @return X coordinate
     */
    public double getX() {
        return this.x;
    }


    /**
     * Gets points Y coordinate
     * @return Y coordinate
     */
    public double getY() {
        return this.y;
    }


    /**
     * Sets point X of coordinate
     * @param x value of X coordinate
     */
    public void setX(double x) {
        this.x = x;
    }


    /**
     * Sets point Y of coordinate
     * @param y value of Y coordinate
     */
    public void setY(double y) {
        this.y = y;
    }


    /**
     * Counts the difference between two X parts of coordinates (this.x - c.x)
     * @param c second coordinate
     * @return difference between the two X parts of coordinates
     */
    public double diffX(MyCoordinate c) {
        return this.x - c.getX();
    }


    /**
     * Counts the difference between two Y parts of coordinates (this.y - c.y)
     * @param c  second coordinate
     * @return difference between the two Y parts of coordinates
     */
    public double diffY(MyCoordinate c) {
        return this.y - c.getY();
    }


    /**
     * Counts the size of vector between two points
     * @param end second coordinate
     * @return size of vector
     */
    public double countVector(MyCoordinate end)
    {
        double size =  Math.pow(this.diffX(end),2) + Math.pow(this.diffY(end),2);
        return Math.sqrt(size);
    }

    /**
     * Overriding method toString for reading from YML
     * @return string that represents this class
     */
    @Override
    public String toString() {
        return "MyCoordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Overriding method for comparing two coordinates
     * @param o Object to compare with
     * @return true if the objects are the same, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyCoordinate that = (MyCoordinate) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0;
    }

    /**
     * Conversion when overriding equals
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
