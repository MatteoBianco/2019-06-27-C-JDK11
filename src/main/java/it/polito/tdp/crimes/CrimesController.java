/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.OffenseIdEdge;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<LocalDate> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<OffenseIdEdge> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String category = this.boxCategoria.getValue();
    	LocalDate date = this.boxGiorno.getValue();
    	if(category == null || date == null) {
    		txtResult.setText("Errore, selezionare una categoria di reato e una data.\n");
    		return;
    	}
    	this.model.creaGrafo(category, date);
    	txtResult.appendText("Grafo creato, con " + this.model.getVerticesSize()
    			+ " vertici e " + this.model.getEdgesSize() + " archi.\n\n");
    	
    	List<OffenseIdEdge> output = this.model.getEdgesBelowMedian();
    	if(output == null) {
    		txtResult.appendText("Nessun arco presente, impossibile stampare gli archi con peso "
    				+ "inferiore del peso mediano.\n");
    		return;
    	}
    	if(output.isEmpty()) {
    		txtResult.appendText("Nessun arco con peso inferiore al peso mediano presente.\n");
    		return;
    	}
    	txtResult.appendText(String.format("Peso mediano del grafo: %.2f\n\n", this.model.getGraphWeightMedian()));
    	for(OffenseIdEdge e : output) {
    		txtResult.appendText(String.format("%s - %s - %d\n", e.getV1(), e.getV2(), e.getWeight()));
    	}
    	this.boxArco.setDisable(false);
    	this.btnPercorso.setDisable(false);
    	this.boxArco.getItems().clear();
    	this.boxArco.getItems().addAll(output);
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	OffenseIdEdge selected = this.boxArco.getValue();
    	if(selected == null) {
    		txtResult.setText("Errore, selezionare un vertice per lanciare la ricorsione.\n");
    		return;
    	}
    	txtResult.appendText("Percorso: \n\n");
    	for(String v : this.model.getPath(selected)) {
    		txtResult.appendText(v + "\n");
    	}
    }
    
    @FXML
    void doDisabilitaPercorso(ActionEvent event) {
    	this.boxArco.getItems().clear();
    	this.boxArco.setDisable(true);
    	this.btnPercorso.setDisable(true);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(this.model.getAllCategories());
    	this.boxGiorno.getItems().addAll(this.model.getAllDates());
    	this.boxArco.setDisable(true);
    	this.btnPercorso.setDisable(true);
    }
}
