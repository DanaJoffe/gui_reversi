package reversi_app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * controls settings screen
 */
public class SettingsController {
  
  static final int DEFAULT_BOARD_SIZE = 8;
  static final String DEFAULT_COLOR_1 = "Black";
  static final String DEFAULT_COLOR_2 = "White";
  @FXML private Button exit_settings;
  @FXML private Button save;
  @FXML private ChoiceBox<String> color_menu_1;
  @FXML private ChoiceBox<String> color_menu_2;
  @FXML private ChoiceBox<Integer> board_size;
  
  private ObservableList<String> color_options = 
      FXCollections.observableArrayList("Red", "Orange", "Yellow", "Green",
                                  "Blue", "Purple", "HotPink", "White", 
                                  "Brown", "Black", "Silver");
  private ObservableList<Integer> board_size_options = 
      FXCollections.observableArrayList(4, 5, 6, 7, 8, 9, 10, 11, 12,
                                    13, 14, 15, 16, 17, 18, 19, 20);
  /**
   * initialize settings screen with color options and board size options.
   */
  @FXML
  private void initialize() {
    //get current settings values
    String color_1 = SettingsController.colorsSettings().get(0); 
    String color_2 = SettingsController.colorsSettings().get(1);
    Integer size = SettingsController.boardSizeSettings();
    //set choice boxes for each setting
    color_menu_1.setItems(color_options);
    color_menu_1.setValue(color_1); 
    color_menu_2.setItems(color_options);
    color_menu_2.setValue(color_2);
    board_size.setItems(board_size_options);
    board_size.setValue(size);
  }

  /**
   * remove color chosen for one player from the 
   * other player's list of color options menu
   * @param event - a color was chosen for one of the players
   */
  @FXML
  private void removeColorFromOtherMenu(ActionEvent event) {
    ObservableList<String> edited_color_options = 
        FXCollections.observableArrayList(color_options);
    String color_1 = color_menu_1.getValue();
    String color_2 = color_menu_2.getValue();
    if (event.getSource() == color_menu_1) {    
      edited_color_options.remove(color_1);
      color_menu_2.setItems(edited_color_options);
      color_menu_2.setValue(color_2);
    } else if (event.getSource() == color_menu_2) {
      edited_color_options.remove(color_2);
      color_menu_1.setItems(edited_color_options);
      color_menu_1.setValue(color_1);
    }     
  }
 
  /**
   * close settings screen
   * @param event - exit was clicked
   */
  @FXML
  private void closeSettings(ActionEvent event) {
    Stage stage = (Stage)exit_settings.getScene().getWindow();
    stage.close();
  }
	
  /**
   * save chosen settings to file
   * @param event - save was clicked
   */
  @FXML
  private void saveSettings(ActionEvent event) {
    //chosen settings values
    String color1 = color_menu_1.getValue();
    String color2 = color_menu_2.getValue();
    Integer b_size = board_size.getValue();    
    //save settings in file    
    PrintWriter os = null;
    try {
      os = new PrintWriter(new OutputStreamWriter(new FileOutputStream("settings.txt")));
      os.println(color1);
      os.println(color2);
      os.println(b_size);
    } catch (IOException e) {
      System.out.println("Error saving settings to file.");
    } finally {
      if (os != null) {
        os.close();
      }
    } 
  }
  /**
   * read settings saved in file
   * file format:
   * <1st player's color>
   * <2nd player's color>
   * <board size>
   * @return list of values in settings if they exist, else return null
   */
  public static List<String> readSettingsFromFile() {
    InputStream is;
    BufferedReader reader = null;
    List<String> settings = new ArrayList<String>();
    String line = null;
    try {
      is = new FileInputStream("settings.txt");
      reader = new BufferedReader(new InputStreamReader(is));
      while((line = reader.readLine()) != null) {
        settings.add(line);
      }
    } catch (IOException e) {
      return null;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          System.out.println("Error closing settings file.");
        }
      }
    }
    return settings;
  }
  
  /**
   * get settings of player's colors.
   * read pre-existing color settings from file, or use default colors
   * @return list of strings of the colors
   */
  public static List<String> colorsSettings() {
    List<String> colors = new ArrayList<String>();
    List<String> settings = SettingsController.readSettingsFromFile();
    if (settings != null) {
      colors.add(settings.get(0));
      colors.add(settings.get(1));
    } else {
      colors.add(DEFAULT_COLOR_1);
      colors.add(DEFAULT_COLOR_2);
    }
    return colors;
  }
  
  /**
   * get setting of board size
   * read size from file if exists, or use default size
   * @return board size
   */
  public static Integer boardSizeSettings() {
    List<String> settings = SettingsController.readSettingsFromFile();
    Integer size;
    if (settings != null) {
      size = Integer.valueOf(settings.get(2));
    } else {
      size = DEFAULT_BOARD_SIZE;
    }
    return size;
  }  
}