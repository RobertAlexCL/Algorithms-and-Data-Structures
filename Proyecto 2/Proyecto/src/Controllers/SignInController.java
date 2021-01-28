package Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SignInController implements Initializable {

	private Main main;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField phoneTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField emailTextField;
	@FXML
	private DatePicker birthDay;
	
	private String name;
	private String phone;
	private String password;
	private String email;
	private String birth;
	
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
	
	public void crearCuenta(ActionEvent event) throws IOException {	
		Boolean verificado = verificarDatos();
		if (verificado == true) {
			//Se crea el servicio de la base de datos
			GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
			registerShutdownHook(db);
			Transaction tx = db.beginTx();
			try {
				Result result = db.execute(
						  "MATCH (u:User)" +
						  "WHERE u.name='"+ name +"'" +
						  "RETURN u.name");
				tx.success();
				if(result.hasNext()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error al crear usuario");
					alert.setHeaderText("Cambia el nombre del usuario");
					alert.setContentText("El nombre del usuario tiene que ser unico");
					
					alert.showAndWait();
				}else {
					//Se crea el nodo del usuario
			    	Node node = db.createNode(Label.label("User"));
			    	node.setProperty("name", name);
			    	node.setProperty("phone", phone);
			    	node.setProperty("password", password);
			    	node.setProperty("email", email);
			    	node.setProperty("birth", birth);
			    	tx.success();
					//Se inicia sesion
			    	main = new Main();
				}
			} finally {
				tx.close();
				db.shutdown();
				main.changeToUserEdit(name);
			}
		}else if (verificado == false) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error en datos ingresado");
			alert.setContentText("Verifica tus datos ingresados");

			alert.showAndWait();
		}
	}
	public Boolean verificarDatos() {
		try {
			name = nameTextField.getText();
			if(name==null)
				return false;
			email = emailTextField.getText();	
			if(email==null)
				return false;
			password = passwordTextField.getText();
			if(password==null)
				return false;
		    int phoneInt = Integer.parseInt(phoneTextField.getText());
		    phone = phoneTextField.getText();
		    //se encuentra la fecha
			LocalDate localDate = birthDay.getValue();
			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
			birth = dateFormat.format(date);  
		    return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
}
	
