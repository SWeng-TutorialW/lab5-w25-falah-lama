package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {
    private SimpleClient client;
    private static Scene scene;
    private boolean stop = false;


    @Override
    public void start(Stage stage) throws IOException {
    	//EventBus.getDefault().register(this);
        //client = SimpleClient.getClient("0", 0);
        //client.openConnection();
        scene = new Scene(loadFXML("secondary"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("The Game");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }



    @Override
    public void stop() {
        System.out.println("Stopped");

        if (stop) {
            return;
        }
        stop = true;

        try {
            SimpleClient clientInstance = SimpleClient.getClient("0", 0);

            if (clientInstance != null && clientInstance.isConnected()) {
                clientInstance.sendToServer("remove client");
            }
            clientInstance.closeConnection();
                super.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
    	Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.WARNING,
        			String.format("Message: %s\nTimestamp: %s\n",
        					event.getWarning().getMessage(),
        					event.getWarning().getTime().toString())
        	);
        	alert.show();
    	});
    	
    }

	public static void main(String[] args) {
        launch();
    }

}