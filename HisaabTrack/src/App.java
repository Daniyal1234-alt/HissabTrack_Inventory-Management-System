//import java.lang.ModuleLayer.Controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            HisaabTrack system = new HisaabTrack();
            //system.start();
            // Create an instance of the controller
            Controller controller = new Controller(system);

            // Get the scene from the controller
            Scene scene = controller.openApp();

            // Set up the stage
            primaryStage.setTitle("HissabTrack");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        launch(args); 
    }
}
