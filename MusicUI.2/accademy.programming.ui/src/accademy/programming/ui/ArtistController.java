package accademy.programming.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ArtistController {

    @FXML
    private Label title;

    @FXML
    private TextField name;




    public String getNewName() {
        String newName = name.getText();
        return newName;
    }

    public void setArtistName(String oldName){
        title = new Label("Edit Artist");
        name.setText(oldName);
    }

    public String addArtist(){
        title = new Label("Add artist");
        return getNewName();
    }

    public String addAlbum(String artist){
        title = new Label("Add Album of " + artist);
        return getNewName();
    }



}
