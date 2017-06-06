package com.paul.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.google.inject.Inject;
import com.paul.Main;
//import com.paul.util.RtmUtil;
import com.paul.service.segment.SegmentCodeService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SegmentController extends BorderPane implements Initializable {
	
	private Main mainApp;
	
	@Inject
	SegmentCodeService segmentCodeService;
	
	@FXML
	TextField xmlLocation;
	
	@FXML
	TextField newXmlLocation;
	
	@FXML
	Button generateXmlButton;
	
	File xmlFile;
	File newDirectory;
	
	@FXML
	private void selectXmlLocationOnAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select XML file");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
		xmlFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if(xmlFile != null){
			xmlLocation.setText(xmlFile.getPath());
		}
	}
	
	@FXML
	private void selectNewXmlLocationOnAction(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Location to save new XML");
		newDirectory = directoryChooser.showDialog(mainApp.getPrimaryStage());
		if(newDirectory != null){
			newXmlLocation.setText(newDirectory.getPath());
		}
	}
	
	@FXML
	private void generateXmlWithSegmentCodeOnAction(ActionEvent event) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(segmentCodeService.appendConditionSegmentCodes(xmlFile));
			File resultFile = new File(newDirectory.getPath() + File.separator + "segment_code_" + xmlFile.getName());
			StreamResult result = new StreamResult(resultFile);
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.transform(source, result);
		    Stage stage = (Stage) generateXmlButton.getScene().getWindow();
		    stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setMain(Main mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}
