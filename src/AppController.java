
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Database.DBExceptions.NoResult;
import Database.DBExceptions.TooManyResults;
import Database.SqliteDB;
import Database.Word;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class AppController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Word> table;
    private ObservableList<Word> data;

    @FXML // fx:id="definition"
    private Text definition; // Value injected by FXMLLoader

    @FXML // fx:id="searchBar"
    private TextField searchBar; // Value injected by FXMLLoader

    /**
     * search selected word
     * @param event mouse over
     */
    @FXML
    void searchInput(KeyEvent event) {
        SqliteDB sqldb = new SqliteDB();
        try {
            List<Word> searchResultList = sqldb.searchInitialEV(searchBar.getText());
            data = FXCollections.observableArrayList(searchResultList);
            table.setItems(data);
            TableColumn<Word, String> resultCol = new TableColumn<>("Kết quả");
            resultCol.setCellValueFactory(
                    new PropertyValueFactory<>(searchResultList.get(0).wordTargetProperty().getName())
            );
            table.getColumns().setAll(resultCol);
            definition.setText("ấn vào từ trong danh sách để xem giải nghĩa");
        } catch (IllegalArgumentException | IllegalStateException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * show result for selected word
     * @param event mouse over
     */
    @FXML
    void showWord(MouseEvent event) {
        String selectedWord = table.getSelectionModel().getSelectedItem().getWord();
        SqliteDB database = new SqliteDB();
        Word word;
        try {
            word = database.searchWordEV(selectedWord);
            definition.setText(word.getExplain());
        } catch (TooManyResults tooManyResults) {
            tooManyResults.printStackTrace();
        } catch (NoResult noResult){
            definition.setText("No result for selected word");
        }
    }


    @FXML
    void initialize() {
        table = new TableView<>();
        assert searchBar != null : "fx:id=\"searchBar\" was not injected: check your FXML file 'NewApp.fxml'.";
        assert definition != null : "fx:id=\"definition\" was not injected: check your FXML file 'NewApp.fxml'.";
    }
}
