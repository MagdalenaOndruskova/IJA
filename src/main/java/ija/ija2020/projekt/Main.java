/*
  Project: Map of city public transport

  Author: Magdaléna Ondrušková <xondru16@stud.fit.vutbr.cz>
  Version: 1.0
  Date: 13.05.2020
 */
package ija.ija2020.projekt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import ija.ija2020.projekt.myPublicTransportMap.MyMap;
import ija.ija2020.projekt.myPublicTransportMap.MyStop;
import ija.ija2020.projekt.myPublicTransportMap.MyStreet;
import ija.ija2020.projekt.myPublicTransportMap.MyVehicle;
import ija.ija2020.projekt.publicTransportMap.Drawable;
import ija.ija2020.projekt.publicTransportMap.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the application
 * @version 1.0
 * @author Magdaléna Ondrušková
 */
public class Main extends Application {

    /**
     * Function that is called when starting the application
     * Function loads all the information from YML and sets the scene for drawing
     * At the end, function starts time (and with that all the vehicles start moving)
     * @param primaryStage  Main window of application
     * @throws Exception Any exception that can happend while running application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiLayout.fxml"));
        BorderPane root_element = loader.load(); // načítanie root elementu guička - v našom prípade Border Pane
        Scene scene = new Scene(root_element);
        primaryStage.setScene(scene); // načítanie guička do primaryStage
        primaryStage.show();

        MainController controller = loader.getController();
        List<Drawable> elements = new ArrayList<>();

        YAMLFactory map_base = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(map_base);
        MyMap street_map = mapper.readValue(new File("./data/map_base.yml"), MyMap.class);
        elements.addAll(street_map.getStreets());
        elements.addAll(street_map.getStops());

        for (MyStreet street : street_map.getStreets())
        {
            for (MyStop stop : street.getStopsOnStreet())
                stop.setStreet(street_map);
        }

        controller.setElements(elements);
        for (MyVehicle vehicle : street_map.getVehicles())
        {
            vehicle.settings();
            controller.setItemsUpdatingInTime(vehicle);
        }
        controller.setMap(street_map);
        controller.starTime(1, controller);
    }
}
