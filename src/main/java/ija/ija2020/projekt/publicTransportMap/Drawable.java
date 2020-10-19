/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt.publicTransportMap;

import javafx.scene.shape.Shape;
import java.util.List;


/**
 * Interface that is implemented by all the objects that will be drawn on the map
 *
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
public interface Drawable {

    /**
     * Function returns gui of the object
     * @return gui List of shapes
     */
    List<Shape> getGui();
}
