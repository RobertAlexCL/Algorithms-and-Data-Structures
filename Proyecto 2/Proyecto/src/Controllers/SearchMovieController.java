package Controllers;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


public class SearchMovieController {

	private Main main;
	
	@FXML	
	private FlowPane searchFlowPane;
	@FXML	
	private TextField movieNameTextField;
	@FXML
	private Button account;
	@FXML
	private Button movie;
	@FXML
	private Button search;
	@FXML
	private Button list;
	
	private String movieName;
	
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
	
	public void buscarPeliculas() {
		
		Boolean verificado = verificarDatos();
		if (verificado == true) {
			//Se crea el servicio de la base de datos
			GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
			registerShutdownHook(db);
			Transaction tx = db.beginTx();
			try {
				Result result = db.execute(
						  "MATCH (m:Movie)" +
						  "WHERE m.name =~ '.*"+ movieName +".*'" +
						  "RETURN m.name, m.id");
				tx.success();
				if(result.hasNext()) {
					searchFlowPane.getChildren().clear();
					
					while (result.hasNext()) {
						
						Map<String, Object> user = result.next();
						String movieName = (String) user.get("m.name");
						String movieId = (String) user.get("m.id");
						
						Label label = new Label(movieName + "   ");
						label.setFont(new Font(18));
						Button button = new Button();
						button.setId(movieId);
						button.setText("Ver Película");
						button.setStyle("-fx-background-color: lime;");
						button.setOnAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								// TODO Auto-generated method stub
								Button currentButton = (Button)event.getSource();
								String movieId = currentButton.getId();
								main = new Main();
								main.changeToSelectedMovie(userLoggedIn, movieId);
							}
						});
						Region p = new Region();
						p.setPrefSize(497.0, 4.0);
						Line line = new Line(0, 0, 500, 0);
						Region p1 = new Region();
						p1.setPrefSize(497.0, 4.0);
						//Se agregan al FlowPane
						searchFlowPane.getChildren().add(label);
						searchFlowPane.getChildren().add(button);
						searchFlowPane.getChildren().add(p);
						searchFlowPane.getChildren().add(line);
						searchFlowPane.getChildren().add(p1);
						
					};
					
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Error al buscar la película");
					alert.setContentText("No hay ninguna palícula con ese nombre");
					alert.showAndWait();
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
	public void setUserLoggedIn(String userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}



	public Boolean verificarDatos() {
		try {
			movieName = movieNameTextField.getText();
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
		
		//Botones
		try {
			
			ImageView accountImage = new ImageView(new Image(this.getClass().getResource("/images/account.png").toString()));
			accountImage.setFitHeight(50);
			accountImage.setFitWidth(50);
			account.setGraphic(accountImage);
			
			ImageView movieImage = new ImageView(new Image(this.getClass().getResource("/images/movie.png").toString()));
			movieImage.setFitHeight(50);
			movieImage.setFitWidth(50);
			movie.setGraphic(movieImage);
			movie.setDisable(true);
			
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
