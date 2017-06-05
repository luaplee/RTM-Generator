package com.paul.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.paul.Main;
import com.paul.controls.TableCellCheckComboBox;
import com.paul.model.CalcGroup;
import com.paul.model.RtmResult;
import com.paul.model.Rule;
import com.paul.util.RtmUtil;
import com.paul.util.TableUtil;
import com.paul.util.XmlToPojo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController extends BorderPane implements Initializable {
	
	private Main mainApp;
	private CalcGroup calcGroup = new CalcGroup();

	@FXML
	private TextField xmlLocation;
	@FXML
	private TextField segmentCode;
	
	@FXML
	private Button loadButton;
	@FXML
	private Button generateButton;
	
	@FXML
	private TableView<RtmResult> rtmTable;
	
	@FXML
	private TableColumn<RtmResult, String> ruleName;
	@FXML
	private TableColumn<RtmResult, String> conditionSet;
	@FXML
	private TableColumn<RtmResult, String> keyCondition;
	@FXML
	private TableColumn<RtmResult, List<String>> keyParameters;
	@FXML
	private TableColumn<RtmResult, String> buildId;
	
	@FXML
	private void generateButtonOnAction(ActionEvent event) {
		List<Rule> eligibleRules = RtmUtil.getEligibleRules(calcGroup, segmentCode.getText());
		ObservableList<RtmResult> rtmResult = FXCollections.observableArrayList(RtmUtil.getRtmResult(eligibleRules));
		
		rtmTable.setEditable(true);
		keyParameters.setEditable(true);
		keyParameters.setCellValueFactory(cellData -> cellData.getValue().getKeyParametersProperty());
		keyParameters.setCellFactory(TableCellCheckComboBox.forTableColumn());
		
		ruleName.setEditable(true);
		ruleName.setCellValueFactory(cellData -> cellData.getValue().getRuleNameProperty());
		ruleName.setCellFactory(TextFieldTableCell.forTableColumn());
		ruleName.setOnEditCommit(
			    new EventHandler<CellEditEvent<RtmResult, String>>() {
			        @Override
			        public void handle(CellEditEvent<RtmResult, String> t) {
			            System.out.println("test");
			        }
			    });
		conditionSet.setCellValueFactory(cellData -> cellData.getValue().getConditionSetProperty());
		keyCondition.setCellValueFactory(cellData -> cellData.getValue().getKeyConditionProperty());
		buildId.setCellValueFactory(cellData -> cellData.getValue().getBuildIdProperty());
		rtmTable.setItems(rtmResult);
	}
	
	@FXML
	private void loadButtonOnAction(ActionEvent event) {
		System.out.println("Load btn event");
		System.out.println(xmlLocation);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select XML file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML", "*.xml")
				);
		File xmlFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if(xmlFile != null){
			xmlLocation.setText(xmlFile.getPath());
			calcGroup =	(CalcGroup) XmlToPojo.getPojo(xmlFile, calcGroup);
		}
	}
	
	@FXML
	private void openSegmentWindow(ActionEvent event) {
		Stage stage;
		Parent root;
		System.out.println("start");
		stage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/com/paul/view/SegmentWindow.fxml"));
			SegmentController segmentController = new SegmentController();
			segmentController.setMain(mainApp);
			loader.setController(segmentController);
			root = (BorderPane) loader.load();
			stage.setScene(new Scene(root));
			stage.setTitle("My modal window");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(loadButton.getScene().getWindow());
			stage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		rtmTable.getSelectionModel().setCellSelectionEnabled(true);
		rtmTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtil.installCopyPasteHandler(rtmTable);
	}

}
