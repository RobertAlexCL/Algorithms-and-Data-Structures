package Controllers;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));;
			Scene scene = new Scene(root,400,550);
			primaryStage.setTitle("Proyecto2");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToUserEdit(String userName) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/UserEdit.fxml"));
			Parent newScene = loader.load();
			//Se envian los datos del usuario
			UserEditController uec = loader.getController();
			uec.setUserLoggedIn(userName);
			uec.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void changeToLogin() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/Login.fxml"));
			Parent newScene = loader.load();
			//Se envian los datos del usuario
			
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void changeToSearch(String userName) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/SearchUser.fxml"));
			Parent newScene = loader.load();
			
			//Se envian los datos del usuario
			SearchUserController suc = loader.getController();
			suc.setUserLoggedIn(userName);
			suc.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToMovie(String userName) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/SearchMovie.fxml"));
			Parent newScene = loader.load();
			
			//Se envian los datos del usuario
			SearchMovieController smc = loader.getController();
			smc.setUserLoggedIn(userName);
			smc.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToSelectedMovie(String userName, String movieId) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/SelectedMovie.fxml"));
			Parent newScene = loader.load();
			
			//Se envian los datos del usuario
			SelectedMovieController ssmc = loader.getController();
			ssmc.setUserLoggedIn(userName);
			ssmc.setMovieId(movieId);
			ssmc.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToMyList(String userName) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/MyList.fxml"));
			Parent newScene = loader.load();
			
			//Se envian los datos del usuario
			MyListController mlc = loader.getController();
			mlc.setUserLoggedIn(userName);
			mlc.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToRecommendedByList(String userName) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/RecommendedByList.fxml"));
			Parent newScene = loader.load();
			
			//Se envian los datos del usuario
			RecommendedByListController rblc = loader.getController();
			rblc.setUserLoggedIn(userName);
			rblc.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToRecommendedByMovie(String userName, String movieId) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/RecommendedByMovie.fxml"));
			Parent newScene = loader.load();
			
			//Se envian los datos del usuario
			RecommendedByMovieController rbmc = loader.getController();
			rbmc.setUserLoggedIn(userName);
			rbmc.setMovieId(movieId);
			rbmc.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToList(String userName, String selectedUserName) {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/Views/List.fxml"));
			Parent newScene = loader.load();
			
			//Se envian los datos del usuario
			ListController lc = loader.getController();
			lc.setUserLoggedIn(userName);
			lc.setSelectedUserName(selectedUserName);
			lc.llenarClase();
			
			Scene scene = new Scene(newScene,400,550);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
