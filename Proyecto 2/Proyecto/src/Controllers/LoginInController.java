package Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class LoginInController {

	@FXML
    private Main main;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField passwordTextField;
	
	private String name;
	private String password;
	
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
	
	public void login(ActionEvent event) throws IOException {	
		Boolean verificado = verificarDatos();
		if (verificado == true) {
			//Se crea el servicio de la base de datos
			GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
			registerShutdownHook(db);
			Transaction tx = db.beginTx();
			String userName = "";
			try {
				Result result = db.execute(
						  "MATCH (u:User)" +
						  "WHERE u.name='"+ name +"'  and u.password='" + password +"'" +
						  "RETURN u.name");
				tx.success();
				if(result.hasNext()) {
					//Se inicia sesion
					Map<String, Object> user = result.next();
					main = new Main();
					userName = (String) user.get("u.name");
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Error al iniciar sesion");
					alert.setContentText("Verifica que los datos esten correctos");
					
					alert.showAndWait();
				}				
			} finally {
				tx.close();
				db.shutdown();
				if(main!=null) {
					main.changeToUserEdit(userName);
				}
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
			password = passwordTextField.getText();
			if(password==null)
				return false;
		    return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	public void crearCuenta(ActionEvent event) throws IOException {	
		Parent newScene = FXMLLoader.load(getClass().getResource("/Views/SignIn.fxml"));
		Scene scene = new Scene(newScene,400,550);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
}
	
