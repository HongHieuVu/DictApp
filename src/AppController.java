
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Database.DBExceptions.NoResult;
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

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Word> table = new TableView<>();
    private ObservableList<Word> data;

    @FXML // fx:id="definition"
    private Text definition;

    @FXML // fx:id="searchBar"
    private TextField searchBar;

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
        List<Word> word;
        try {
            word = database.searchWordEV(selectedWord);
            StringBuilder displayText = new StringBuilder();
            for (Word result : word){
                displayText.append(result.getExplain()).append("\n");
            }
            definition.setText(displayText.toString());
        } catch (NoResult noResult){
            definition.setText("No result for selected word");
        }
    }


    @FXML
    void initialize() {
        assert searchBar != null : "fx:id=\"searchBar\" was not injected: check your FXML file 'NewApp.fxml'.";
        assert definition != null : "fx:id=\"definition\" was not injected: check your FXML file 'NewApp.fxml'.";
    }
}
