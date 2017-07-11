package com.paul.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.google.inject.Inject;
import com.paul.Main;
import com.paul.controls.TableCellCheckComboBox;
import com.paul.model.CalcGroup;
import com.paul.model.RtmResult;
import com.paul.model.Rule;
import com.paul.service.MainService;
import com.paul.util.TableUtil;
import com.paul.util.XmlToPojo;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private List<RtmResult> rtmTableContents = new ArrayList<>();
	
	@Inject
	SegmentController segmentController;
	
	@Inject
	MainService mainService;

	@FXML
	private TextField xmlLocation;
	@FXML
	private TextField segmentCode;
	
	@FXML
	private Button loadButton;
	@FXML
	private Button generateButton;
	@FXML
	private Button exportExcelButton;
	
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
	private Label totalRowCount;
	@FXML
	private Label totalSelectedRowCount;
	
	@FXML
	private void generateButtonOnAction(ActionEvent event) {
		if(!calcGroup.isEmpty()){
			List<Rule> eligibleRules = mainService.getEligibleRules(calcGroup, segmentCode.getText());
			rtmTableContents = mainService.getRtmResult(eligibleRules);
			ObservableList<RtmResult> rtmResult = FXCollections.observableArrayList(rtmTableContents);
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
						}
					});
			conditionSet.setCellValueFactory(cellData -> cellData.getValue().getConditionSetProperty());
			keyCondition.setCellValueFactory(cellData -> cellData.getValue().getKeyConditionProperty());
			buildId.setCellValueFactory(cellData -> cellData.getValue().getBuildIdProperty());
			totalRowCount.setText(String.valueOf(rtmResult.size()));
			totalSelectedRowCount.setText("0");
			rtmTable.setItems(rtmResult);
		}
	}
	
	
	@FXML
	private void exportExcelButtonOnAction(ActionEvent event) {
		if(!rtmTableContents.isEmpty()){
			//TODO: remove try catch and find elegant way of throwing exception to front end, also add logger
			try{
				File fileLocation = mainService.getNewFileLocation(mainApp.getPrimaryStage(), "Excel File Location");
				File excelFile = new File(fileLocation.getPath() 
											+ File.separator 
											+ "RTM" 
											+ new Date().getTime() 
											+ ".xlsx");
				mainService.exportExcelFile(rtmTableContents, excelFile);
			} catch(Exception e){
				//TODO: handle access is denied
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void loadButtonOnAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select XML file");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
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
		stage = new Stage();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getClassLoader().getResource("com/paul/view/SegmentWindow.fxml"));
			segmentController.setMain(mainApp);
			loader.setController(segmentController);
			root = (BorderPane) loader.load();
			stage.setScene(new Scene(root));
			stage.setTitle("My modal window");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(loadButton.getScene().getWindow());
			stage.showAndWait();
		} catch (IOException e) {
			// TODO use logger
			e.printStackTrace();
		}

	}
	
	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rtmTable.getSelectionModel().setCellSelectionEnabled(true);
		rtmTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtil.installCopyPasteHandler(rtmTable);
		
		rtmTable.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<RtmResult>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends RtmResult> c) {
				while(c.next()){
					String selectedRowCount = String.valueOf(rtmTable.getSelectionModel().getSelectedItems().size());
					totalSelectedRowCount.setText(selectedRowCount);
				}
			}

			
		});
	}

}
