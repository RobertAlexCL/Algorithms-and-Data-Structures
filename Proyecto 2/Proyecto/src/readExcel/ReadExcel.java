package readExcel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import Models.Movie;
 
public class ReadExcel {
 
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
	
	public static void main(String[] args) {
		
		//Se crea el servicio de la base de datos
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(new File("moviesDb/"));
		registerShutdownHook(db);
		
		
		ArrayList<Movie> movies = new ArrayList<Movie>();	
		
		String nombreArchivo = "tmdb_5000_credits.csv";
		String rutaArchivo = ".\\Exceldb\\" + nombreArchivo;
		String nombreArchivo2 = "tmdb_5000_movies.csv";
		String rutaArchivo2 = ".\\Exceldb\\" + nombreArchivo2;
 
		List<List<String>> records = new ArrayList<>();
		List<List<String>> records2 = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
			try (BufferedReader br2 = new BufferedReader(new FileReader(rutaArchivo2))) {
				//Archivo 1:
				String line;
			    while ((line = br.readLine()) != null) {
			        String[] values = line.split("\\s*,\\s*");
			        records.add(Arrays.asList(values));
			    }
			    int counter = 0;
			    for (int i = 1; i < records.size(); i++) {
			    	String id = records.get(i).get(0);
			    	String name = records.get(i).get(1);
			    	if(name.contains("\""))
			    		name = name.substring(1);
			    	String actor = "";	
			    	String director = "";
			    	String producer = "";
			    	if(name.contains("\""))
			    		name = name.substring(1);
					for (int j = 2; j < records.get(i).size(); j++) {
						if(records.get(i).get(j).contains("character") && records.get(i).size()>j+4 && records.get(i).get(j+4).contains("name") && actor == "")
			    			actor = records.get(i).get(j+4);
						if(records.get(i).get(j).contains("\"Director\"") && records.get(i).size()>j+1 && records.get(i).get(j+1).contains("name") && director == "") {
							String[] str = records.get(i).get(j+1).split("\"\"");
							director = str[3];
						}
						if(records.get(i).get(j).contains("\"Producer\"") && records.get(i).size()>j+1 && records.get(i).get(j+1).contains("name") && producer == "") {
							String[] str = records.get(i).get(j+1).split("\"\"");
							producer = str[3];
						}
					}
					
				    String[] actors = actor.split("\"\"");
					if(actors.length >= 3) {
						actor = actors[3];
					}else {
						actor = "";
					}
					
					if(actor!="" && director!="" && producer!="") {
						counter = counter + 1;
						Movie currentMovie = new Movie(id, name, "", actor, director, producer, "", "", "");
						movies.add(currentMovie);
					}
				}
			    System.out.println(counter);
			    
			    
			  //Archivo 2:
				String line2;
			    while ((line2 = br2.readLine()) != null) {
			        String[] values2 = line2.split("\\s*,\\s*");
			        records2.add(Arrays.asList(values2));
			    }
			    for (int i = 1; i < records2.size(); i++) {
			    	int j = 1;
			    	String currentId = "";
			    	String qualification = records2.get(i).get(records2.get(i).size() - 2);	
			    	String date = "";
			    	String popularity = "";
			    	String genre = records2.get(i).get(2);
			    	if(genre.contains("name")) {
			    		String[] genres = genre.split("\"");
						if(genres.length >= 3) {
							genre = genres[6];
						}else {
							genre = "";
						}
			    	}else {
						genre = "";
					}
			    	while((currentId == "" || date == ""  || popularity == "") && j<=records2.get(i).size()) {
			    		try {
			    			if(currentId == "") {
								int cid = Integer.parseInt(records2.get(i).get(j));
								currentId = records2.get(i).get(j);
			    			}else if(popularity == ""){
			    				double cpopularity = Double.parseDouble(records2.get(i).get(j));
			    				popularity = records2.get(i).get(j);
			    			}
							long cdate = Date.parse(records2.get(i).get(j));
							date = records2.get(i).get(j);
			    		} catch (Exception e) {
							// TODO: handle exception
			    			j = j +1;
						}
			    	}
			    	if(currentId != "" && qualification != "" && date != "" && popularity != "" && genre != "") {
			    		for (int k = 0; k < movies.size(); k++) {
							if(movies.get(k).getId().equals(currentId)) {
								Movie currentMovie = movies.get(k);
								currentMovie.setQualification(qualification);
								currentMovie.setGenre(genre);
								currentMovie.setDate(date);
								currentMovie.setPopularity(popularity);
								movies.set(k, currentMovie);
							}
						}
			    	}
				}
			    for (int i = 0; i < movies.size(); i++) {
			    	if(movies.get(i).getQualification().equals("")) {
			    		movies.remove(i);
			    		counter = counter - 1;
			    	}
				}
			    for (int i = 0; i < movies.size(); i++) {
			    	System.out.println(movies.get(i).toString());
			    	
			    	Transaction tx = db.beginTx();
			    	try {
			    		//Se crea el nodo de cada pelicula
				    	Node node = db.createNode(Label.label("Movie"));
				    	node.setProperty("id", movies.get(i).getId());
				    	node.setProperty("name", movies.get(i).getName());
				    	node.setProperty("genre", movies.get(i).getGenre());
				    	node.setProperty("actor", movies.get(i).getActor());
				    	node.setProperty("director", movies.get(i).getDirector());
				    	node.setProperty("producer", movies.get(i).getProducer());
				    	node.setProperty("date", movies.get(i).getDate());
				    	node.setProperty("popularity", movies.get(i).getPopularity());
				    	node.setProperty("qualification", movies.get(i).getQualification());
				    	tx.success();
			    	} finally {
						// TODO: handle finally clause
						tx.close();
					}
				}
			    System.out.println(counter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
}
