/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Business start = cmbLocale.getValue();
    	if(start==null) {
    		txtResult.setText("Selezionare un locale");
    		return;
    	}
    	
    	double soglia;
    	try {
    		soglia = Double.parseDouble(txtX.getText());
    	} catch(NumberFormatException e) {
    		txtResult.setText("La soglia deve essere un numero  con virgola compreso tra 0 e 1");
    		return;
    	}
    	
    	if(soglia<0 || soglia>1) {
    		txtResult.setText("La soglia deve essere un numero  con virgola compreso tra 0 e 1");
    		return;
    	}
    	
    	List<Business> percorso = model.calcolaPercorso(start, soglia);
    	if(percorso==null) {
    		txtResult.setText("Nessun percorso disponibile");
    		return;
    	}
    	
    	txtResult.appendText("PERCORSO MIGLIORE:\n");
    	for(Business locale : percorso) {
    		txtResult.appendText(locale+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String city = cmbCitta.getValue();
    	Integer anno = cmbAnno.getValue();
    	if(city==null || anno==null) {
    		txtResult.setText("Selezionare una città e un anno");
    		return;
    	}
    	
    	String msg = model.creaGrafo(city, anno);
    	txtResult.setText(msg);
    	
    	cmbLocale.getItems().clear();
    	cmbLocale.getItems().addAll(model.getVertici());
    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
    	txtResult.clear();
    	Business locale = model.getLocaleMigliore();
    	if(locale==null) {
    		txtResult.setText("Devi prima creare il grafo");
    		return;
    	}
    	
    	txtResult.appendText("LOCALE MIGLIORE: "+locale);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        for(int anno=2005; anno<=2013; anno++) {
        	cmbAnno.getItems().add(anno);
        }
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCitta.getItems().addAll(model.getCities());
    }
}
