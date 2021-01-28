package Controllers;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


public class UserEditController {

	private Main main;
	
	@FXML	
	private TextField username;
	@FXML
	private TextField phone;
	@FXML
	private TextField email;
	@FXML
	private Button account;
	@FXML
	private Button movie;
	@FXML
	private Button search;
	@FXML
	private Button list;

	private String nombre;
	private String numero;
	private String correoe;
	
	private String userLoggedIn;
	
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
	
	public void guardarUsuario() {
		
		Boolean verificado = verificarDatos();
		if (verificado == true) {
			//Se crea el servicio de la base de datos
			GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
			registerShutdownHook(db);
			Transaction tx = db.beginTx();
			try {
				Result result = db.execute(
						  "MATCH (u:User)" +
						  "WHERE u.name='"+ nombre +"'" +
						  "RETURN u.name");
				tx.success();
				if(result.hasNext()) {
					Map<String, Object> user = result.next();
					String userName = (String) user.get("u.name");
					if(!userLoggedIn.equals(userName)) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Error en actualizar");
						alert.setContentText("El nombre del uzuario ya ha sido utilizado");
	
						alert.showAndWait();
					}else {
						Result result1 = db.execute("MATCH (u:User)" +
								  "WHERE u.name='"+ userLoggedIn +"'" +
								  " SET u.name='"+ nombre +"'" +
								  " SET u.phone='"+ numero +"'" +
								  " SET u.email='"+ correoe +"'" +
								  " RETURN u.name");
						tx.success();
						if(result1.hasNext()) {
							userLoggedIn = nombre;
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Éxito");
							alert.setHeaderText("Se ha actualizado la información del usuario");
							alert.showAndWait();
						}
					}
				}else {
					Result result1 = db.execute("MATCH (u:User)" +
							  "WHERE u.name='"+ userLoggedIn +"'" +
							  " SET u.name='"+ nombre +"'" +
							  " SET u.phone='"+ numero +"'" +
							  " SET u.email='"+ correoe +"'" +
							  " RETURN u.name");
					tx.success();
					if(result1.hasNext()) {
						userLoggedIn = nombre;
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Éxito");
						alert.setHeaderText("Se ha actualizado la información del usuario");
						alert.showAndWait();
					}
				}
			} finally {
				tx.close();
				db.shutdown();
			}
		}else if (verificado == false) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error en datos ingresados");
			alert.setContentText("Verifica tus datos ingresados");

			alert.showAndWait();
		}
	}
	
	
	
	/**
	 * @return the userLoggedIn
	 */
	public String getUserLoggedIn() {
		return userLoggedIn;
	}



	/**
	 * @param userLoggedIn the userLoggedIn to set
	 */
	public void setUserLoggedIn(String userName) {
		this.userLoggedIn = userName;
	}



	public Boolean verificarDatos() {
		try {
			nombre = username.getText();
			int numeroInt = Integer.parseInt(phone.getText());			
			numero = phone.getText();
			correoe = email.getText();
		    return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}
	
	public void goToAccount(){
		
		main = new Main();
		main.changeToUserEdit(userLoggedIn);
		
	}
	
	public void salirUsuario(){
		
		main = new Main();
		main.changeToLogin();
		
	}

	public void goToMovie(){
		
		main = new Main();
		main.changeToMovie(userLoggedIn);
		
	}
	
	public void goToSearch(){
		
		main = new Main();
		main.changeToSearch(userLoggedIn);
		
	}
	
	public void goToList(){
		
		main = new Main();
		main.changeToMyList(userLoggedIn);
		
	}
	
	public void llenarClase() {
		
		//Se crea el servicio de la base de datos
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("moviesDb/"));
		registerShutdownHook(db);
		Transaction tx = db.beginTx();
		try {
			Result result = db.execute(
					  "MATCH (u:User)" +
					  "WHERE u.name='"+ userLoggedIn +"'" +
					  "RETURN u.name, u.phone, u.email");
			tx.success();
			if(result.hasNext()) {
				Map<String, Object> user = result.next();
				username.setText((String) user.get("u.name"));
				phone.setText((String) user.get("u.phone"));
				email.setText((String) user.get("u.email"));
			}
		}finally {
			tx.close();
			db.shutdown();
			//Botones
			try {
				ImageView accountImage = new ImageView(new Image(this.getClass().getResource("/images/account.png").toString()));
				accountImage.setFitHeight(50);
				accountImage.setFitWidth(50);
				account.setGraphic(accountImage);
				account.setDisable(true);
				
				ImageView movieImage = new ImageView(new Image(this.getClass().getResource("/images/movie.png").toString()));
				movieImage.setFitHeight(50);
				movieImage.setFitWidth(50);
				movie.setGraphic(movieImage);
				
				ImageView searchImage = new ImageView(new Image(this.getClass().getResource("/images/search.png").toString()));
				searchImage.setFitHeight(50);
				searchImage.setFitWidth(50);
				search.setGraphic(searchImage);
	
				ImageView listImage = new ImageView(new Image(this.getClass().getResource("/images/list.png").toString()));
				listImage.setFitHeight(50);
				listImage.setFitWidth(50);
				list.setGraphic(listImage);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
}
