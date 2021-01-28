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


public class SelectedMovieController {

	private Main main;
	
	private GraphDatabaseService db;
	
	@FXML
	private Button account;
	@FXML
	private Button movie;
	@FXML
	private Button search;
	@FXML
	private Button list;
	@FXML
	private Button agregarLista;
	@FXML
	private Label name;
	@FXML
	private Label date;
	@FXML
	private Label actor;
	@FXML
	private Label genre;
	@FXML
	private Label director;
	@FXML
	private Label producer;
	@FXML
	private Label qualification;
	@FXML
	private Label popularity;
	
	private String movieId;
	
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
	
	public void buscarPelicula() {
		//Se crea el servicio de la base de datos
		registerShutdownHook(db);
		Transaction tx = db.beginTx();
		try {
			Result result = db.execute(
					  "MATCH (m:Movie)" +
					  "WHERE m.id = '"+ movieId +"'" +
					  "RETURN m.name, m.genre, m.date, m.actor, m.director, m.producer, m.qualification, m.popularity");
			tx.success();
			if(result.hasNext()) {
				Map<String, Object> movie = result.next();
				
				name.setText((String) movie.get("m.name"));
				date.setText((String) movie.get("m.date"));
				actor.setText("Actor: "+ (String) movie.get("m.actor"));
				genre.setText("Género: "+ (String) movie.get("m.genre"));
				director.setText("Director: "+ (String) movie.get("m.director"));
				producer.setText("Productor: "+ (String) movie.get("m.producer"));
				qualification.setText("Calificación: "+ (String) movie.get("m.qualification"));
				popularity.setText("Popularidad: "+ (String) movie.get("m.popularity"));
				
				Result result2 = db.execute(
						  "MATCH (u:User) -[:LIKES]-> (m:Movie)" + 
						  "WHERE m.id='"+ movieId +"' and u.name='"+ userLoggedIn +"'" + 
						  "RETURN u.name");
				tx.success();
				if(result2.hasNext()) {
					agregarLista.setText("Eliminar de mi Lista");
					agregarLista.setStyle("-fx-background-color: red;");
					agregarLista.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							// TODO Auto-generated method stub
							registerShutdownHook(db);
							Transaction tx = db.beginTx();
							try {
								Result result3 = db.execute(
										  "MATCH (u:User) -[l:LIKES]-> (m:Movie)" + 
										  "WHERE m.id='"+ movieId +"' and u.name='"+ userLoggedIn +"'" + 
										  "DELETE l " + 
										  "RETURN u.name");
								tx.success();
								if(result3.hasNext()) {
									buscarPelicula();
								}
							} finally {
								// TODO: handle finally clause
								tx.close();
							}							
						}
					});
				}else {
					agregarLista.setText("Agregar a mi Lista");
					agregarLista.setStyle("-fx-background-color: lime;");
					agregarLista.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							// TODO Auto-generated method stub
							registerShutdownHook(db);
							Transaction tx = db.beginTx();
							try {
								Result result3 = db.execute(
										  "MATCH (u:User) WHERE u.name='"+ userLoggedIn +"'" + 
										  "MATCH (m:Movie) WHERE m.id='"+ movieId +"'" + 
										  "CREATE (u) -[:LIKES]-> (m)" +
										  "RETURN u.name");
								tx.success();
								if(result3.hasNext()) {
									buscarPelicula();
								}
							} finally {
								// TODO: handle finally clause
								tx.close();
							}
						}
					});
				}
			}
		} finally {
			tx.close();
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
	
	/**
	 * @return the movieId
	 */
	public String getMovieId() {
		return movieId;
	}

	/**
	 * @param movieId the movieId to set
	 */
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	
	public void goToRecommendedByMovie(){
		
		db.shutdown();
		main = new Main();
		main.changeToRecommendedByMovie(userLoggedIn, movieId);
		
	}

	public void goToAccount(){

		db.shutdown();
		main = new Main();
		main.changeToUserEdit(userLoggedIn);
		
	}
	
	public void goToMovie(){

		db.shutdown();
		main = new Main();
		main.changeToMovie(userLoggedIn);
		
	}
	
	public void goToSearch(){

		db.shutdown();
		main = new Main();
		main.changeToSearch(userLoggedIn);
		
	}
	
	public void goToList(){

		db.shutdown();
		main = new Main();
		main.changeToMyList(userLoggedIn);
		
	}
	
	public void llenarClase() {
		
		db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("./moviesDb/"));
		
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
		
		buscarPelicula();
	}
	
}
