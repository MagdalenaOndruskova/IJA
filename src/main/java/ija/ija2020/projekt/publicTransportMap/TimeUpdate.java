/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt.publicTransportMap;

import java.time.LocalTime;

/**
 * Interface holds all the function that are called whenever the timer ticks
 *
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
public interface TimeUpdate {

    /**
     * Function that is called with the main controller to update all objects position on the map
     * @param time Current time
     * @param controller Main Controller from which was the function called
     */
    void update(LocalTime time, MainController controller);

    /**
     * Function that is called when user jumps in the time
     * @param jumpedInTime Time to where I am jumping to
     */
    void jumpInTime(LocalTime jumpedInTime);

    /**
     * Function that controls traffic of the street on which is the vehicle at the moment
     */
    void checkTraffic();

    /**
     * Function that controls if vehicle is at the stop,
     * if yes, it will stop the vehicle on the stop for some time
     */
    void stopAtStop();
}
