package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.characters.*;
import model.characters.Character;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;



import java.io.IOException;
import java.util.ArrayList;



import org.w3c.dom.Node;



import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;

public class Main extends Application implements EventHandler<ActionEvent> {
	public static Stage Window;
	public static Scene s1, s2;
	public static Character character;
	public static Cell cell;
    public static Hero selectedHero;
	///update:
	public static ArrayList<Pair<Button, Hero>> AvailHeroButtons = new ArrayList< Pair< Button, Hero>>();

	//button is the key
	//hero is the value
	
	
	
	public void start(Stage primaryStage) {
        try {
			Game.loadHeroes("Heroes.csv");
			} 
        catch (IOException e1) {
			e1.printStackTrace();
			}

		
		//try {
        	Window = primaryStage;
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Window.setScene(scene);
			
			//CreateScene1();
			//HBox h = DisplayAvailHeroes();
			 

//			 b.setOnAction(e-> {
//				 Window.setScene(s2);
//				});
		    
			//s1 = new Scene(h,400,400);
			CreateScene1();
			
			Window.setScene(s1);
			Window.show();
	//	} catch(Exception e) {
		//	e.printStackTrace();
		//}
	}
	
	
	public static void CreateScene1() {
		HBox h = DisplayAvailHeroes();
		s1 = new Scene(h,400,400);		
	}

	
	
	
	public static HBox DisplayAvailHeroes() {
//		 
		HBox H = new HBox();
		for(int i =0; i<Game.availableHeroes.size(); i++) {
			Button b = new Button(Game.availableHeroes.get(i).toString());
			H.getChildren().add(b);
			AvailHeroButtons.add(new Pair<Button, Hero>(b, Game.availableHeroes.get(i)));
			
			//update
			b.setOnAction(e -> {
				Button clicked = (Button) e.getSource();
				Game.startGame(handleStartGame(clicked));
				selectedHero = handleStartGame(clicked);
//				System.out.print(Game.heroes.get(0).getName());
//				System.out.print(Game.heroes.size());
				createscene2();
				 Window.setScene(s2);


			});
		
	}
		return H;

	}
	
	
	public static VBox DisplayHeroes() {
		VBox V = new VBox();
//		System.out.println(Game.heroes.size());

		for(int i =0; i<Game.heroes.size(); i++) {
			Button l = new Button(Game.heroes.get(i).getName());
			V.getChildren().add(l);	
			int f = i;
			l.setOnMouseEntered(e ->{
				l.setText(Game.heroes.get(f).toString());
			});
			l.setOnMouseExited(e ->{
				l.setText(Game.heroes.get(f).getName());
			});
			l.setOnAction(e -> {
			    selectedHero = Game.heroes.get(f);
			    createscene2();
			 });  
			
		}
		
		return V;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

	/////update
	public static Hero handleStartGame(Button clicked) {
		Hero h = null;
		for(int i =0; i<AvailHeroButtons.size(); i++) {
			if(clicked.equals( AvailHeroButtons.get(i).getKey()))
			{	
				h = AvailHeroButtons.get(i).getValue();
			break;
			}
	}
		return h;

	}
	
	public static GridPane map(){
		GridPane G = new GridPane();
		for (int i = 14; i >= 0 ; i--) {
			for (int j = 0; j < Game.map[i].length; j++) {
				
				Button button = new Button();
				 button.setPrefSize(40, 40);
				 G.add(button,j,14-i);
				 //Game.startGame(Game.availableHeroes.get(0));
				 
				 if (((Game.map[i][j] instanceof CharacterCell && ((CharacterCell) Game.map[i][j]).getCharacter()== null)
						 || Game.map[i][j] instanceof TrapCell ) &&  Game.map[i][j].isVisible()){
					 button.setText("E");
				
				 }
				 
				 if ((Game.map[i][j] instanceof CharacterCell && ((CharacterCell) Game.map[i][j]).getCharacter() instanceof Hero)){
					 button.setText(((CharacterCell) Game.map[i][j]).getCharacter().getName());
					 int x = i;
					 int y = j; 
					 if(selectedHero != null){
						 Hero h = (Hero)((CharacterCell)Game.map[x][y]).getCharacter();
						 button.setOnAction(e -> {
							 selectedHero.setTarget(h);
						 });
					 	}
					 }
				 
				 if ((Game.map[i][j] instanceof CharacterCell && ((CharacterCell) Game.map[i][j]).getCharacter() instanceof Zombie)&&  Game.map[i][j].isVisible()){
					 button.setText("Z");
					
					 if(selectedHero != null){
						 Zombie z = (Zombie) ((CharacterCell) Game.map[i][j]).getCharacter();
						 button.setOnAction(e -> {
						    selectedHero.setTarget(z);
						 });  
					 }	 
				 }
				 if ((Game.map[i][j] instanceof CollectibleCell && ((CollectibleCell)Game.map[i][j]).getCollectible() instanceof Vaccine)&&  Game.map[i][j].isVisible()){
					 button.setText("V");
				 }
				 if ((Game.map[i][j] instanceof CollectibleCell && ((CollectibleCell)Game.map[i][j]).getCollectible() instanceof Supply)&&  Game.map[i][j].isVisible()){
					 button.setText("S");
				 }
				 
				 
				
			}
			}
		
		
		
		return G;
		
	}
	
	public static HBox controller(){
		HBox h = new HBox();
		Button attackButton = new Button("attack");
		Button moveButton =  new Button("move");
		Button cureButton =  new Button("cure");
		Button usespecialButton =  new Button("use special");
		Button endturnButton =  new Button("end turn");
		h.getChildren().addAll(attackButton,moveButton,cureButton,usespecialButton , endturnButton);
		
		attackButton.setOnAction(e -> {
			 try {
				 selectedHero.attack();
				 createscene2();
    	   		 createscene3();
					} 
		        catch (NotEnoughActionsException e1 ) {
					//e1.getMessage();
					Alert alert = new Alert(AlertType.NONE);
			        alert.setTitle("Pop-up Message");
			        alert.setHeaderText(null);
			        alert.setContentText(e1.getMessage());

			     // Enable the close button
			        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

			        // Set the result to close the dialog when the close button is clicked
			        alert.setResult(ButtonType.CLOSE);

			        // Show the alert and wait for it to be closed
			        alert.showAndWait();
					}
			    catch(InvalidTargetException e1){
			    	Alert alert = new Alert(AlertType.NONE);
			        alert.setTitle("Pop-up Message");
			        alert.setHeaderText(null);
			        alert.setContentText(e1.getMessage());

			     // Enable the close button
			        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

			        // Set the result to close the dialog when the close button is clicked
			        alert.setResult(ButtonType.CLOSE);

			        // Show the alert and wait for it to be closed
			        alert.showAndWait();			    }
			 });
			 
			 
			 moveButton.setOnAction(k -> {
				// try {
				 
				 
				direction();

		});
			 
			 
			 
			 usespecialButton.setOnAction(e -> {
				 try {
					 
					((Hero) selectedHero).useSpecial();
					 createscene2();
	    	   		 createscene3();
						} 
			        catch (NoAvailableResourcesException e1 ) {
						//e1.getMessage();
						Alert alert = new Alert(AlertType.NONE);
				        alert.setTitle("Pop-up Message");
				        alert.setHeaderText(null);
				        alert.setContentText(e1.getMessage());

				     // Enable the close button
				        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

				        // Set the result to close the dialog when the close button is clicked
				        alert.setResult(ButtonType.CLOSE);

				        // Show the alert and wait for it to be closed
				        alert.showAndWait();	
				        }
				 
				    catch(InvalidTargetException e1){
				    	Alert alert = new Alert(AlertType.NONE);
				        alert.setTitle("Pop-up Message");
				        alert.setHeaderText(null);
				        alert.setContentText(e1.getMessage());

				     // Enable the close button
				        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

				        // Set the result to close the dialog when the close button is clicked
				        alert.setResult(ButtonType.CLOSE);

				        // Show the alert and wait for it to be closed
				        alert.showAndWait();					    }
				 });
			
			 
			 cureButton.setOnAction(e -> {
				 try {
					((Hero) selectedHero).cure();
					 createscene2();
//	    	   		 createscene3();
						} 
			        catch (NoAvailableResourcesException e1 ) {
						//e1.getMessage();
						Alert alert = new Alert(AlertType.NONE);
				        alert.setTitle("Pop-up Message");
				        alert.setHeaderText(null);
				        alert.setContentText(e1.getMessage());

				     // Enable the close button
				        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

				        // Set the result to close the dialog when the close button is clicked
				        alert.setResult(ButtonType.CLOSE);

				        // Show the alert and wait for it to be closed
				        alert.showAndWait();							}
				    catch(InvalidTargetException e1){
				    	Alert alert = new Alert(AlertType.NONE);
				        alert.setTitle("Pop-up Message");
				        alert.setHeaderText(null);
				        alert.setContentText(e1.getMessage());

				     // Enable the close button
				        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

				        // Set the result to close the dialog when the close button is clicked
				        alert.setResult(ButtonType.CLOSE);

				        // Show the alert and wait for it to be closed
				        alert.showAndWait();					    }
				 catch(NotEnoughActionsException e1){
				    	Alert alert = new Alert(AlertType.NONE);
				        alert.setTitle("Pop-up Message");
				        alert.setHeaderText(null);
				        alert.setContentText(e1.getMessage());

				     // Enable the close button
				        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

				        // Set the result to close the dialog when the close button is clicked
				        alert.setResult(ButtonType.CLOSE);

				        // Show the alert and wait for it to be closed
				        alert.showAndWait();					 }
				 });
				
			 endturnButton.setOnAction(e -> {
					 try {
						 Game.endTurn();
						 createscene2();
		    			 createscene3();

							} 
				        catch (NotEnoughActionsException e1 ) {
							//e1.getMessage();
							Alert alert = new Alert(AlertType.NONE);
					        alert.setTitle("Pop-up Message");
					        alert.setHeaderText(null);
					        alert.setContentText(e1.getMessage());

					     // Enable the close button
					        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

					        // Set the result to close the dialog when the close button is clicked
					        alert.setResult(ButtonType.CLOSE);

					        // Show the alert and wait for it to be closed
					        alert.showAndWait();
							}
					    catch(InvalidTargetException e1){
					    	Alert alert = new Alert(AlertType.NONE);
					        alert.setTitle("Pop-up Message");
					        alert.setHeaderText(null);
					        alert.setContentText(e1.getMessage());

					     // Enable the close button
					        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

					        // Set the result to close the dialog when the close button is clicked
					        alert.setResult(ButtonType.CLOSE);

					        // Show the alert and wait for it to be closed
					        alert.showAndWait();			    }
					 });
		
		
		return h;
	}
	
	
	
//	 public static void Scene2(){
//		 
//		 //Label l =  new  Label("hii");
//		 VBox v = DisplayHeroes();
//		 GridPane G = map();
//		 BorderPane bp = new BorderPane();
//		 bp.setRight(v);
//		 bp.setLeft(G);
//		 //Scene ss4 =  new Scene(bp,400,400);
//		 
//		 //return ss4;
//	 }
	 public static void  createscene2() {
		 BorderPane b = new BorderPane();
		 b.setLeft(map());
		 
		 HBox h = new HBox();
		 h.getChildren().addAll(controller());
		 b.setTop(h);
		 
		 VBox v = new VBox();
		 v.getChildren().addAll(DisplayHeroes(), new Label("Currently playing : \n"+selectedHero.toString()));
		 b.setRight(v);
		 
		 
		 s2 = new Scene (b);
		 Window.hide();
		 Window.setScene(s2);
		 Window.show();
	 }
	 
	 
	 
	 
	public static void direction() {
		Alert moveAlert = new Alert(AlertType.INFORMATION);
        moveAlert.setTitle("Direction Dialog");
        moveAlert.setHeaderText("Select a direction");
        
        

        // Create buttons for each direction
        ButtonType upButton = new ButtonType("Up", ButtonData.YES);
        ButtonType downButton = new ButtonType("Down", ButtonData.YES);
        ButtonType rightButton = new ButtonType("Right", ButtonData.YES);
        ButtonType leftButton = new ButtonType("Left", ButtonData.YES);
       // movee.getButtonTypes().setAll(upButton, downButton, rightButton, leftButton);
        moveAlert.getButtonTypes().setAll(upButton, downButton, rightButton, leftButton);
//        movee.show();
        moveAlert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Set the result to close the dialog when the close button is clicked
        moveAlert.setResult(ButtonType.CLOSE);
        int x = selectedHero.getLocation().x;
        int y = selectedHero.getLocation().y;
        

        moveAlert.showAndWait().ifPresent(response -> {
            if (response == upButton) {
            	try {
            		checkTrapCell(x+1 , y);
    				selectedHero.move(Direction.UP);
    				createscene2();
    				createscene3();

    			} catch (NotEnoughActionsException e1 ) {
    				//e1.getMessage();
    				Alert alert = new Alert(AlertType.WARNING);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();				}
    		    catch(MovementException e1){
    		    	Alert alert = new Alert(AlertType.NONE);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();		   
    		        }
            }
            
            if (response == downButton) {
            	try {
            		checkTrapCell(x-1 , y);
    				selectedHero.move(Direction.DOWN);
    				createscene2();
    				createscene3();

    			} catch (NotEnoughActionsException e1 ) {
    				//e1.getMessage();
    				Alert alert = new Alert(AlertType.WARNING);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();				}
    		    catch(MovementException e1){
    		    	Alert alert = new Alert(AlertType.NONE);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();		   
    		        }
            }
            
            
            if (response == leftButton) {
            	try {
            		checkTrapCell(x, y-1);
    				selectedHero.move(Direction.LEFT);
    				createscene2();
    				createscene3();

    			} catch (NotEnoughActionsException e1 ) {
    				//e1.getMessage();
    				Alert alert = new Alert(AlertType.WARNING);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();				}
    		    catch(MovementException e1){
    		    	Alert alert = new Alert(AlertType.NONE);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();		   
    		        }
            }
            
            
            
            if (response == rightButton) {
            	try {
            		checkTrapCell(x , y+1);
    				selectedHero.move(Direction.RIGHT);
    				createscene2();
    				createscene3();
    			} catch (NotEnoughActionsException e1 ) {
    				//e1.getMessage();
    				Alert alert = new Alert(AlertType.WARNING);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();				}
    		    catch(MovementException e1){
    		    	Alert alert = new Alert(AlertType.NONE);
    		        alert.setTitle("Pop-up Message");
    		        alert.setHeaderText(null);
    		        alert.setContentText(e1.getMessage());

    		        // Enable the close button
    		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

    		        // Set the result to close the dialog when the close button is clicked
    		        alert.setResult(ButtonType.CLOSE);

    		        // Show the alert and wait for it to be closed
    		        alert.showAndWait();		   
    		        }
            }
            
            
        });
    
}
        
        public static void  createscene3() {   		 
	   		 if(Game.checkWin()) {
	   			 BorderPane b = new BorderPane();
	   			 Label l = new Label("YOU WON");
	   			 b.setCenter(l);
	   			 Scene s3 = new Scene(b);
	   	   		 Window.hide();
	   	   		 Window.setScene(s3);
	   	   		 Window.show();
	   	   		 return;
	   		 }
	   		 if(Game.checkGameOver()) {
	   			 BorderPane b = new BorderPane();
	   			 Label l = new Label("Game Over");
	   			 b.setCenter(l);
	   			 Scene s3 = new Scene(b);
	   	   		 Window.hide();
	   	   		 Window.setScene(s3);
	   	   		 Window.show();
	   	   		 return;
	   		 }
   	   		 return;
        }
        
        
        public static void checkTrapCell(int x , int y) {
    		if ((x < 0 || y < 0 || x > Game.map.length - 1 || y > Game.map.length - 1) || 
    				(selectedHero.getActionsAvailable() < 1 ))
    			return;
    		
    		else {
    			
        	if(Game.map[x][y] instanceof TrapCell) {
        		Alert alert = new Alert(AlertType.WARNING);
		        alert.setTitle("Pop-up Message");
		        alert.setHeaderText(null);
		        alert.setContentText("You entered a trap cell");
		        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		        // Set the result to close the dialog when the close button is clicked
		        alert.setResult(ButtonType.CLOSE);
		        // Show the alert and wait for it to be closed
		        alert.showAndWait();		
        	}
        	
    		}
        	
        }
        
        
        
        
        
        
        
        // Add the buttons to an HBox layout
//        HBox buttonBox = new HBox(10, upButton, downButton, rightButton, leftButton);
//        buttonBox.setAlignment(Pos.CENTER);
//
//        dialog.getDialogPane().setContent(buttonBox);
//        Button cancelButton = new Button("Cancel");
//        cancelButton.setOnAction(event -> dialog.close());
//        
//        //((Button) dialog).setCancelButton(cancelButton);
//
//        // Show the dialog
//        dialog.showAndWait();
//
//
//        return dialog;

//		Dialog<Button> dialog = new Dialog<>();
//        dialog.setTitle("Custom Dialog");
//
//		
//		DialogPane dialogPane = dialog.getDialogPane();
//
//        // Create buttons
////        ButtonType button1 = new ButtonType("Button 1");
////        ButtonType button2 = new ButtonType("Button 2");
////        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL);
//		
//		BorderPane bp = new BorderPane();
//        //bp.setPadding(new Insets(10));
//        //vbox.setSpacing(10);
//
//        // Create buttons and add them to the VBox
//        Button up = new Button("up");
//        Button down = new Button("down");
//        Button right = new Button("right");
//        Button left = new Button("left"); 
//      //  bp.getChildren().addAll(up, down, right,left);
//        bp.setBottom(down);
//        bp.setTop(up);
//        bp.setLeft(left);
//        bp.setRight(right);

        // Add buttons to the dialog pane
//        dialogPane.getButtonTypes().add(bp);
//
//        // Create a layout for the dialog content
//        VBox content = new VBox();
//        // Add additional UI components to the layout if needed
//
//        dialogPane.setContent(content);
//
//        // Handle button actions
//        dialog.setResultConverter(buttonType -> {
//            if (buttonType == button1) {
//                System.out.println("Button 1 clicked");
//            } else if (buttonType == button2) {
//                System.out.println("Button 2 clicked");
//            }
//            return buttonType;
//        });

		
		
		
	
//     public static Popup directionpopup() {
//    	 Popup popup = new Popup();
//
//         // Create a VBox layout container for the buttons
//         BorderPane bp = new BorderPane();
//         //bp.setPadding(new Insets(10));
//         //vbox.setSpacing(10);
//
//         // Create buttons and add them to the VBox
//         Button up = new Button("up");
//         Button down = new Button("down");
//         Button right = new Button("right");
//         Button left = new Button("left"); 
//       //  bp.getChildren().addAll(up, down, right,left);
//         bp.setBottom(down);
//         bp.setTop(up);
//         bp.setLeft(left);
//         bp.setRight(right);
//
//         // Set the content of the popup to be the VBox
//         popup.getContent().add(bp);
//
//         // Event handler for the openButton
//         up.setOnAction(event -> {
//             // Show the popup below the openButton
//             //popup.show(openButton, 0, 0);
//			 try {
//				selectedHero.move(Direction.UP);
//			} catch (NotEnoughActionsException e1 ) {
//				//e1.getMessage();
//				Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//				}
//		    catch(MovementException e1){
//		    	Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//		    }
//   
//});
//         down.setOnAction(event -> {
//			 try {
//				selectedHero.move(Direction.DOWN);
//			}  catch (NotEnoughActionsException e1 ) {
//				//e1.getMessage();
//				Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//				}
//		    catch(MovementException e1){
//		    	Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//		    }
//   
//});
//         
//         left.setOnAction(event -> {
//			 try {
//				selectedHero.move(Direction.LEFT);
//			}  catch (NotEnoughActionsException e1 ) {
//				//e1.getMessage();
//				Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//				}
//		    catch(MovementException e1){
//		    	Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//		    }
//   
//});
//         
//         right.setOnAction(event -> {
//			 try {
//				selectedHero.move(Direction.RIGHT);
//			}  catch (NotEnoughActionsException e1 ) {
//				//e1.getMessage();
//				Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//				}
//		    catch(MovementException e1){
//		    	Alert alert = new Alert(AlertType.NONE);
//		        alert.setTitle("Pop-up Message");
//		        alert.setHeaderText(null);
//		        alert.setContentText(e1.getMessage());
//
//		        alert.showAndWait();
//		    }
//   
//});
//         return popup;
//    	 
//     }
	 
	
	 
	 
	 
	 
	 
	 
	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
