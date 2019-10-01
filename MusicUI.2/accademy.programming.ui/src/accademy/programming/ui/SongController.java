package accademy.programming.ui;

import accademy.programming.common.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;

//import sample.model.Song;

public class SongController {

    @FXML
    private Label title;

    @FXML
    private TextField name;

    @FXML
    private TableView songsTable;

    @FXML
    private ArrayList<Song> albumSongs;

    @FXML
    private Button buttonUp = new Button();

    @FXML
    private Button buttonDown = new Button();

    @FXML
    public void initialize () {
        Image imageDown = new Image(getClass().getResourceAsStream("arrowdown.png"));
        ImageView arrowDown = new ImageView(imageDown);
        buttonDown.setGraphic(arrowDown);

        Image imageUp = new Image(getClass().getResourceAsStream("arrowup.png"));
        ImageView arrowUp = new ImageView(imageUp);
        buttonUp.setGraphic(arrowUp);

    }



    public void listSongs(ArrayList<Song> albumSongsSelected) {


        albumSongs = albumSongsSelected;
//        for (Song song : albumSongs) {
//            System.out.println("A) " + song.getName());
//        }
        Task<ObservableList<String>> task = new GetAlbumSongTask(albumSongs);
        songsTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    @FXML
    public void trackUp(){
        Song song = (Song) songsTable.getSelectionModel().getSelectedItem();
        int track = song.getTrack();
        if (track > 1) {
            Collections.swap(albumSongs, track-1, track-2);
            albumSongs.get(track-1).setTrack(track);
            albumSongs.get(track-2).setTrack(track-1);

            listSongs(albumSongs);
            songsTable.getSelectionModel().select(song);


            printlist();

        }

    }

    @FXML
    public void trackDown(){
        Song song = (Song) songsTable.getSelectionModel().getSelectedItem();

        int track = song.getTrack();
        if (track < albumSongs.size()) {
            Collections.swap(albumSongs, track-1, track);
            albumSongs.get(track-1).setTrack(track);
            albumSongs.get(track).setTrack(track+1);

            listSongs(albumSongs);
            songsTable.getSelectionModel().select(song);
            printlist();

        }
    }

    public ArrayList<Song> getAlbumSongs() {
        return albumSongs;
    }

    public String getNewName() {
        String newName = name.getText();
        return newName;
    }

    public void setSongName(String oldName){
        title = new Label("Edit Song");
        name.setText(oldName);
    }

    public void addSong(int id){
        title = new Label("Add song");
        Song newSong = new Song();
        if (albumSongs.size() > 0 ) {
            Song lastSong = albumSongs.get(albumSongs.size() - 1);
            newSong.setTrack(lastSong.getTrack() + 1);
        }  else {
            newSong.setTrack(1);
        }
        newSong.setAlbumId(id);
        newSong.setName(getNewName());
        albumSongs.add(newSong);
        listSongs(albumSongs);
    }

    public void songRemove(){
        Song song = (Song) songsTable.getSelectionModel().getSelectedItem();
        int track = song.getTrack();
        albumSongs.remove(track-1);
        for (int i= track+1;i<=albumSongs.size() - 1; i++){
            albumSongs.get(i).setTrack(i-1);
        }

        listSongs(albumSongs);
    }

    private void printlist () {

          for (Song song : albumSongs) {
            System.out.println(song.getTrack() + " " + song.getName());
        }

    }


//    public String addAlbum(String artist){
//        title = new Label("Add Album of " + artist);
//        return getNewName();
//    }



}


class GetAlbumSongTask  extends Task {

    private ArrayList<Song> albumSongs;

    public GetAlbumSongTask(ArrayList<Song> albumSongsList) {
        this.albumSongs = albumSongsList;
    }

    @Override
    public ObservableList<Song> call() {
        return  FXCollections.observableArrayList(albumSongs);

    }


}