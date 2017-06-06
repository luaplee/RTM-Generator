package com.paul;
	
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.paul.controller.MainController;
import com.paul.module.BasicModule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootHome;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("RMT Generator");
			
			initOverview();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void initOverview(){
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/com/paul/view/RmtOverview.fxml"));
			
			Injector injector = Guice.createInjector(new BasicModule());
			MainController mainController = injector.getInstance(MainController.class);
			
			mainController.setMain(this);
			loader.setController(mainController);
			rootHome = (BorderPane) loader.load();
			
//			For future use of css
//			String css = classLoader.getResource("css/style.css").toExternalForm();
//			scene.getStylesheets().add(css);
//			
			Scene scene = new Scene(rootHome);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
}
