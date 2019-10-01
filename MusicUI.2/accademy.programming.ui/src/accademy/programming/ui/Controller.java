package accademy.programming.ui;

import accademy.programming.common.Album;
import accademy.programming.common.Artist;
import accademy.programming.common.Song;
import accademy.programming.db.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

//import accademy.programming.common.Datasource;



public class Controller {

    public Object selectedObj;

    public Artist lastSelectedArtist ;
    public Album lastSelectedAlbum ;
    public Song lastSelectedSong ;


    @FXML
    private TableView artistTable;

    @FXML
    public void listArtists() {

        selectedObj = null;
        lastSelectedAlbum = null;
        lastSelectedArtist = null;

        Task<ObservableList<Artist>> task = new GetAllArtistsTask();
        artistTable.itemsProperty().bind(task.valueProperty());


        new Thread(task).start();
    }


    @FXML
    public void listAlbums() {

        lastSelectedAlbum = null;

        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        final Object selected = selectedObj();
        if (selected instanceof Artist ) {
            lastSelectedArtist = artist;
            listAlbums(artist.getId());
        }

        if (selected == null && lastSelectedArtist != null) listAlbums(lastSelectedArtist.getId());
        else if (artist == null && selectedObj == null) {

            Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
                @Override
                protected ObservableList<Album> call() throws Exception {
                    return FXCollections.observableArrayList(
                            Datasource.getInstance().queryAlbumsSortedByArtist());
                }
            };
            artistTable.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
        }

//        else if (selectedObj == null){
////            selectedObj = artist;
//            listAlbums(lastSelectedArtist.getId());
//        }
        else if (selectedObj instanceof Artist ) {
            Artist artistSelect = (Artist) selectedObj;
            listAlbums(artistSelect.getId());
        }
    }

    private void listAlbums(int artistId) {

        lastSelectedAlbum = null;

        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
            @Override
            protected ObservableList<Album> call() throws Exception {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().queryAlbumsForArtistId(artistId));
            }
        };
        artistTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();

    }

    @FXML
    public void listSongs() {

        final Object selected = selectedObj();


            Task<ObservableList<Song>> task = new Task<ObservableList<Song>>() {
                @Override
                protected ObservableList<Song> call() throws Exception {
                    if (selected == null) {

                        if (lastSelectedAlbum != null) {
                            Album album = (Album) selectedObj;
                            return FXCollections.observableArrayList(
                                    Datasource.getInstance().querySongsByAlbumId(lastSelectedAlbum.getId()));
                        }
                        else if (lastSelectedArtist != null  ) {
                            return FXCollections.observableArrayList(
                                    Datasource.getInstance().querySongsListByArtist(lastSelectedArtist.getId()));
                        }
                        else  {
                            return FXCollections.observableArrayList(
                                    Datasource.getInstance().querySongsList());
                        }

                    } else if (selected instanceof Artist ) {
                        Artist artist = (Artist) selected;
                        lastSelectedArtist = artist;
                        return FXCollections.observableArrayList(
                                Datasource.getInstance().querySongsListByArtist(artist.getId()));

                    } else if (selected instanceof Album) {
                        Album album = (Album) selected;
                        lastSelectedAlbum = album;
//                        selectedObj = album;
                        return FXCollections.observableArrayList(
                                Datasource.getInstance().querySongsByAlbumId(album.getId()));
                    }
                    return null;
                }
            };
            artistTable.itemsProperty().bind(task.valueProperty());

        artistTable.setOnMouseClicked((EventHandler<MouseEvent>) click -> {

            if (click.getClickCount() == 2) {
                //Use ListView's getSelected Item
               if (selectedObj() instanceof Song) {
                   Song songSelected = (Song) artistTable.getSelectionModel().getSelectedItem();
                   //use this to do whatever you want to. Open Link etc.
                    // creare in song getLink : link in youtube of songs
                   String link = "https://www.youtube.com/results?search_query=";
                   String artistLink = songSelected.getName() + "+";
                   try {
                       System.out.println(" id song : " + songSelected.getId());
                       artistLink += (Datasource.getInstance().getArtistName(songSelected.getId()));
                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
                   link += artistLink.replace(" ", "+");
                   openWebpage(link);
               }
            }
        });

            new Thread(task).start();



    }


    @FXML
    public void update() throws SQLException {

        Object selectedObj = selectedObj();

        if (selectedObj instanceof Artist) updateArtist((Artist) selectedObj);
        else if (selectedObj instanceof Album) updateAlbum((Album) selectedObj);
        else if (selectedObj instanceof Song) updateSong((Song) selectedObj);
    }

    private void updateArtist(Artist artist) {

        lastSelectedArtist = artist;
        String newArtistName = artistDialog(artist.getName());

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().updateArtistName(artist.getId(), newArtistName);
            }
        };

        task.setOnSucceeded(e -> {
            if (task.valueProperty().get()) {
                artist.setName(newArtistName);
                artistTable.refresh();
            }
        });

        new Thread(task).start();
    }

    private void updateAlbum(Album album) {

        lastSelectedAlbum = album;
        String newName = artistDialog(album.getName());

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                return Datasource.getInstance().updateAlbumName(album.getId(), newName);
            }
        };

        task.setOnSucceeded(e -> {
            if (task.valueProperty().get()) {
                album.setName(newName);
                artistTable.refresh();
            }
        });

        new Thread(task).start();
    }

    private void updateSong(Song song) {

        lastSelectedSong = song;
        String newName = artistDialog(song.getName());

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                return Datasource.getInstance().updateSongName(song.getId(), newName);
            }
        };

        task.setOnSucceeded(e -> {
            if (task.valueProperty().get()) {
                song.setName(newName);
                artistTable.refresh();
            }
        });

        new Thread(task).start();
    }

    private String artistDialog(String name) {
        String newArtistName = name;
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
//        dialog.initOwner(main.getScene().getWindow());
        dialog.setTitle("Edit Name : " + name);


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("artistdialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return null;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ArtistController artistController = fxmlLoader.getController();
        artistController.setArtistName(name);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            newArtistName = artistController.getNewName();
        }
        return newArtistName;
    }

    @FXML
    public void add() throws SQLException {

         Object selected = selectedObj();

        if (selected == null ) {

            if (selectedObj == null ) {
                if (lastSelectedAlbum == null) addArtist();
                else addSongToAlbum(lastSelectedAlbum);
            }
            else if (selectedObj instanceof Artist) addAlbum((Artist) selectedObj);
            else if (selectedObj instanceof Album) addSongToAlbum((Album) selectedObj);
        }
        else {
            if (selected instanceof Artist) addAlbum((Artist) selected);
            else if (selected instanceof Album) addSongToAlbum((Album) selected);
            else addSongToAlbum(lastSelectedAlbum);
        }
    }

    @FXML
    public void remove() throws SQLException {

        Object selectedObj = selectedObj();


        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setTitle("Remove fom list " + selectedObj.getClass().getName());

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            if (selectedObj instanceof Artist) removeArtist((Artist) selectedObj);
            else if (selectedObj instanceof Album) removeAlbum((Album) selectedObj);
            else if (selectedObj instanceof Song) addSongToAlbum((Song) selectedObj);
        }
    }


    private void addArtist() {

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setTitle("Add artist");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("artistdialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ArtistController artistController = fxmlLoader.getController();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String newArtist = artistController.addArtist();
            addArtist(newArtist);
        }
    }

    private void addAlbum(Artist artist) {

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setTitle("Add album");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("artistdialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
        }
        ButtonType buttonAdd = new ButtonType("Add Album");
        dialog.getDialogPane().getButtonTypes().add(buttonAdd);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ArtistController artistController = fxmlLoader.getController();

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == buttonAdd) {
            String newAlbum = artistController.addAlbum(artist.getName());
            addAlbum(newAlbum, artist.getId());
        }

//        else if (result.isPresent() && result.get() == buttonRemove) {
//            Datasource.getInstance().removeAlbum(album.getId());
//        } else if (result.isPresent() && result.get() == buttonAddSongs) {
//
//            ArrayList<Song> albumSongsList = addSongToAlbum(album);
//
//
//            if (albumSongsList != null) {
//                if (Datasource.getInstance().resetSongs(album.getId())){
//                for (Song albumSong : albumSongsList) {
//                    Datasource.getInstance().insertSong(albumSong.getTrack(), albumSong.getName(), album.getId());
//                    }
//                }
//            }
//        }
    }


    private ArrayList<Song> addSongToAlbum(Object object) throws SQLException {

        Album album = new Album();
        Song song = new Song();
        int albumId = 0;
        int track = 1;
        if (object instanceof Album) {
            album = (Album) object;
            albumId = album.getId();
        } else if (object instanceof Song) {
            song = (Song) object;
            albumId = song.getAlbumId();
            album.setName(Datasource.getInstance().getAlbumName(albumId));
        }

        ArrayList<Song> albumSongsList = new ArrayList<>();

        ArrayList<Song> albumSongs = Datasource.getInstance().querySongsByAlbumId(albumId);

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setTitle("Add songs to " + album.getName());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("songdialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return null;
        }

        ButtonType buttonRemove = new ButtonType("Remove");
        ButtonType buttonAddSongs = new ButtonType("Add Song");
        ButtonType buttonSavelist = new ButtonType("Save SongsList");
        dialog.getDialogPane().getButtonTypes().add(buttonSavelist);
        dialog.getDialogPane().getButtonTypes().add(buttonRemove);
        dialog.getDialogPane().getButtonTypes().add(buttonAddSongs);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        boolean finishedList = false;

        SongController songController = fxmlLoader.getController();
        songController.listSongs(albumSongs);
        if (song != null) songController.setSongName(song.getName());

        Optional<ButtonType> result = dialog.showAndWait();
        while (!finishedList) {

            if (result.isPresent() && result.get() == buttonAddSongs) {
                songController.addSong(albumId);
                result = dialog.showAndWait();
            } else if (result.isPresent() && result.get() == buttonRemove) {
                //remove song selected
                songController.songRemove();
                result = dialog.showAndWait();
            } else if (result.isPresent() && result.get() == buttonSavelist) {

                albumSongsList = songController.getAlbumSongs();
                if (albumSongsList != null) {
                    if (Datasource.getInstance().resetSongs(album.getId())) {
                        for (Song albumSong : albumSongsList) {
                            Datasource.getInstance().insertSong(albumSong.getTrack(), albumSong.getName(), album.getId());
                        }
                    }
                }

                return albumSongsList;
            } else if (result.isPresent() && result.get() == ButtonType.CANCEL) finishedList = true;
        }
        return null;
    }


    private void addArtist(String newArtist) {

        Artist artist = new Artist();

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
//                return isInteger( Datasource.getInstance().insertArtist(newArtist));
                return Datasource.getInstance().insertArtist(newArtist);
            }
        };

        task.setOnSucceeded(e -> {
            if (isInteger(task.valueProperty().getValue())) {
                int newId = task.getValue();
                artist.setName(newArtist);
                artist.setId(newId);
                System.out.println("Artist task : " + newId + newArtist);
                artistTable.refresh();
            }
        });
        new Thread(task).start();
    }

    private void removeArtist(Artist artist) {


        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return Datasource.getInstance().removeArtist(artist.getName());
            }
        };

        task.setOnSucceeded(e -> {
            if (isInteger(task.valueProperty().getValue())) {
                int removedLevel = task.getValue();
                if (removedLevel > 0) {
                    artistTable.getSelectionModel().clearSelection();
                    artistTable.getItems().clear();
                }
                listArtists();
            }
        });

        new Thread(task).start();
    }


    private void addAlbum(String newAlbum, int id) {
        Album album = new Album();

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
//                return isInteger( Datasource.getInstance().insertArtist(newArtist));
                return Datasource.getInstance().insertAlbum(newAlbum, id);
            }
        };

        task.setOnSucceeded(e -> {
            if (isInteger(task.valueProperty().getValue())) {
                int newId = task.getValue();
                album.setName(newAlbum);
                album.setId(newId);
                System.out.println("Album task : " + newId + newAlbum);
                artistTable.refresh();
            }
        });

        new Thread(task).start();
    }


    private void removeAlbum(Album album) {


        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return Datasource.getInstance().removeAlbum(album.getId());
            }
        };

        task.setOnSucceeded(e -> {
            if (isInteger(task.valueProperty().getValue())) {

                if (task.getValue() > 0) {
                    artistTable.getSelectionModel().clearSelection();
                    artistTable.getItems().clear();
                }
                listAlbums(album.getArtistId());
            }
        });

        new Thread(task).start();
    }


    public static boolean isInteger(Object object) {
        if (object instanceof Integer) {
            return true;
        } else {
            String string = object.toString();

            try {
                Integer.parseInt(string);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }


    private Object selectedObj() {

        try {
            Object object = artistTable.getSelectionModel().getSelectedItem();
            if (object instanceof Artist) {
                Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
                return artist;
            } else if (object instanceof Album) {
                Album album = (Album) artistTable.getSelectionModel().getSelectedItem();
                return album;
            } else if (object instanceof Song) {
                Song song = (Song) artistTable.getSelectionModel().getSelectedItem();
                return song;
            }

        } catch (ClassCastException e) {
            e.printStackTrace();

        }
        return null;
    }


    public void unselectItem() {
        artistTable.getSelectionModel().clearSelection();
    }


    public static void openWebpage(String urlString) {
        try {
            java.awt.Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


class GetAllArtistsTask extends Task {

    @Override
    public ObservableList<Artist> call() {
        return FXCollections.observableArrayList
                (Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
    }
}


