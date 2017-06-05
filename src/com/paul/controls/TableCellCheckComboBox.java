package com.paul.controls;

import java.util.List;

import org.controlsfx.control.CheckComboBox;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class TableCellCheckComboBox<S> extends TableCell<S, List<String>> {

    private CheckComboBox<String> checkComboBox;

    public static <S> Callback<TableColumn<S, List<String>>, TableCell<S, List<String>>> forTableColumn() {
        return cell -> new TableCellCheckComboBox<S>();
    }
    
    public TableCellCheckComboBox() {
        super();
        initCheckComboBox();
        setOnKeyReleased(getKeyEventHandler());
    }
    
    
    private void initCheckComboBox() {
        this.focusedProperty().addListener(
        (observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<String> newText = extractNewValue();
		        S myData = (S) getTableRow().getItem();
		        ObservableValue<List<String>> obsValue = getTableColumn().getCellObservableValue(myData);
		        if (obsValue instanceof ListProperty) {
		            ((ListProperty) obsValue).setAll(newText);
		        }
            }
        });
    }

    private List<String> extractNewValue() {
        ObservableList<String> selected = checkComboBox.getCheckModel().getCheckedItems();
        StringBuffer newText = new StringBuffer("");

        for (String txt : selected) {
            if (newText.length() > 0) {
                newText.append(", ");
            }
            newText.append(txt);
        }
        return selected;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            setText(null);
            setGraphic(this.checkComboBox);
            Platform.runLater(() -> {
                if (getGraphic() != null) {
                    checkComboBox.getCheckModel().clearChecks();

                    for (String txt : getItem()) {
                        checkComboBox.getCheckModel().check(txt.trim());
                    }
                }
            });
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        
    	StringBuilder sb = new StringBuilder();
  	  	for(String str : getItem()){
            sb.append(str).append(","); //separating contents using semi colon
        }

        setText(sb.toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(List<String> item, boolean empty) {
    	super.updateItem(item, empty);
    	setItem(item);
    	
    	if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            ObservableList<String> cellItems = FXCollections.observableArrayList(item);
            checkComboBox = new CheckComboBox<String>(cellItems);
            
            for (String txt : item) {
                checkComboBox.getCheckModel().check(txt.trim());
            }

            if (isEditing()) {
                setText(null);
                setGraphic(this.checkComboBox);
            } else {
            	StringBuilder sb = new StringBuilder();
            	  for(String str : extractNewValue()){
                      sb.append(str).append(",");
                  }
                
                setText(sb.toString());
                setGraphic(null);
            }
        }
    }

    private EventHandler<KeyEvent> getKeyEventHandler() {
        return keyEvent -> {
            switch (keyEvent.getCode()) {
            case SCROLL_LOCK:
            case BACK_SPACE:
            case BEGIN:
            case CANCEL:
            case CAPS:
            case CONTROL:
            case NUM_LOCK:
            case F1:
            case F3:
            case F4:
            case F5:
            case F6:
            case F7:
            case F8:
            case F9:
            case F10:
            case F11:
            case F12:
            case INSERT:
            case DELETE:
                break;
            default:
                commitEdit(extractNewValue());
                getTableView().requestFocus();
                break;
            }
        };
    }
    
}