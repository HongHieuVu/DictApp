
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Database.DBExceptions.NoResult;
import Database.Database;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class AppController {

    public javafx.scene.web.WebView WebView = new WebView();
    WebEngine viewer;

    @FXML
    private TableView<Word> table = new TableView<>();

    @FXML // fx:id="searchBar"
    private TextField searchBar;

    /**
     * search selected word
     *
     * @param event mouse over
     */
    @FXML
    void searchInput(KeyEvent event) {
        Database sqldb = new Database();
        try {
            List<Word> searchResultList = sqldb.searchInitialEV(searchBar.getText());
            ObservableList<Word> data = FXCollections.observableArrayList(searchResultList);
            table.setItems(data);
            TableColumn<Word, String> resultCol = new TableColumn<>("Kết quả");
            resultCol.setCellValueFactory(
                    new PropertyValueFactory<>(searchResultList.get(0).wordTargetProperty().getName())
            );
            table.getColumns().setAll(resultCol);
            viewer.loadContent("ấn vào từng từ để xem giải nghĩa");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * show result for selected word. If there are multiple results then they will be appended
     * if different from each other
     *
     * @param event mouse over
     */
    @FXML
    void showWord(MouseEvent event) {
        String selectedWord = table.getSelectionModel().getSelectedItem().getWord();
        Database database = new Database();

        List<Word> word;
        try {
            word = database.searchWordEV(selectedWord);
            StringBuilder displayText = new StringBuilder();
            for (Word result : word) {
                displayText.append(result.getHtml()).append("\n");
            }
            viewer.loadContent(displayText.toString());
        } catch (NoResult noResult) {
            viewer.loadContent("No result for selected word");
        }
    }

    @FXML
    void initialize() {
        viewer = WebView.getEngine();
        viewer.loadContent("ấn vào từng từ để xem giải nghĩa");
        assert searchBar != null : "fx:id=\"searchBar\" was not injected: check your FXML file 'NewApp.fxml'.";
    }
}