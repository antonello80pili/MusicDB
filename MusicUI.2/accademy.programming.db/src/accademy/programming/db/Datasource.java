package accademy.programming.db;

import accademy.programming.common.Album;
import accademy.programming.common.Artist;
import accademy.programming.common.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timbuchalka on 9/12/16.
 */
public class Datasource {

    public static final String DB_NAME = "music.db";

       public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\user\\Desktop\\java projects\\DBMUSIC\\" + DB_NAME;
//    public static final String CONNECTION_STRING = "jdbc:sqlite:/Volumes/Production/Courses/Programs/JavaPrograms/Music/" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
                    " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_SORTED_BY_ARTIST  =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ID + " , " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " , " +
                    TABLE_ALBUMS + '.' + COLUMN_ALBUM_ARTIST + " FROM " + TABLE_ALBUMS +
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " COLLATE NOCASE ";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
                    TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";


    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";

    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
            "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " ORDER BY " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = ?";


    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS +
            '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";

    public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS +
            '(' + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM +
            ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    // SELECT artist.name FROM artists INNER JOIN albums ON artists._id = albums.artist
    //INNER JOIN songs ON songs.album = albums._id
    //WHERE songs.album = ?;

    // TO TEST
    public static final String QUERY_ARTIST_BY_ID = "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " FROM " +
            TABLE_ARTISTS +  " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_ARTISTS+ "." + COLUMN_ARTIST_ID + " = " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST  +  " INNER JOIN " + TABLE_SONGS + " ON "+ TABLE_SONGS + "."  + COLUMN_SONG_ALBUM +
            " = " + TABLE_ALBUMS + "."  + COLUMN_ALBUM_ID +  " WHERE " + TABLE_SONGS + "."  +  COLUMN_SONG_ID + " = ?";

    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";

    public static final String QUERY_ALBUMS_BY_ARTIST_ID = "SELECT * FROM " + TABLE_ALBUMS +
            " WHERE " + COLUMN_ALBUM_ARTIST + " = ? ORDER BY " + COLUMN_ALBUM_NAME + " COLLATE NOCASE";

    public static final String QUERY_ALBUM_BY_ALBUM_ID = "SELECT "  + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS +
            " WHERE " + COLUMN_ALBUM_ID + " = ?";

    public static final String UPDATE_ARTIST_NAME = "UPDATE " + TABLE_ARTISTS + " SET " +
            COLUMN_ARTIST_NAME + " = ? WHERE " + COLUMN_ARTIST_ID + " = ?";

    public static final String UPDATE_ALBUM_NAME = "UPDATE " + TABLE_ALBUMS + " SET " +
            COLUMN_ALBUM_NAME + " = ? WHERE " + COLUMN_ALBUM_ID + " = ?";

    public static final String UPDATE_SONG_NAME = "UPDATE " + TABLE_SONGS+ " SET " +
            COLUMN_SONG_TITLE+ " = ? WHERE " + COLUMN_SONG_ID + " = ?";

    public static final String REMOVE_ARTIST = "DELETE FROM " + TABLE_ARTISTS +  " WHERE " + COLUMN_ARTIST_ID + " = ?";

    public static final String REMOVE_ALBUM = "DELETE FROM " + TABLE_ALBUMS  +  " WHERE " + COLUMN_ALBUM_ID + " = ?";

    public static final String REMOVE_SONGS_OF_ALBUM = "DELETE FROM " + TABLE_SONGS  +  " WHERE " + COLUMN_SONG_ALBUM + " = ?";

    public static final String QUERY_SONGS_BY_ALBUM_ID = "SELECT * FROM " + TABLE_SONGS +
            " WHERE " + COLUMN_SONG_ALBUM + " = ? ORDER BY " + COLUMN_SONG_TRACK + " COLLATE NOCASE";

    public static final String QUERY_SONGS_LIST = "SELECT " +  COLUMN_SONG_ID + "," +  COLUMN_SONG_TRACK+ "," +
            COLUMN_SONG_TITLE + "," + COLUMN_SONG_ALBUM  + " FROM " + TABLE_SONGS +
            " ORDER BY " + COLUMN_SONG_ALBUM + " COLLATE NOCASE";

    public static final String QUERY_SONGS_BY_ARTIST =  "SELECT " + TABLE_SONGS + "."  + COLUMN_SONG_ID + "," +  COLUMN_SONG_TRACK + "," +
             COLUMN_SONG_TITLE + "," + COLUMN_SONG_ALBUM  + " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + '.' +
             COLUMN_ALBUM_ID + " WHERE " + COLUMN_ALBUM_ARTIST + " = ? " +  " ORDER BY " + COLUMN_SONG_ALBUM + " COLLATE NOCASE";




    private Connection conn;

    private PreparedStatement querySongInfoView;

    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;

    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;


    private PreparedStatement queryAlbumsSortedByArtist;
    private PreparedStatement queryAlbumsByArtistId;
    private PreparedStatement updateArtistName;
    private PreparedStatement updateAlbumName;
    private PreparedStatement updateSongName;
    private PreparedStatement removeArtist;
    private PreparedStatement removeAlbum;
    private PreparedStatement removeSongsOfAlbum;
    private PreparedStatement queryArtistById;
    private PreparedStatement querySongsByAlbumId;
    private PreparedStatement queryAlbumNameById;

    private PreparedStatement querySongsList;
    private PreparedStatement querySongsByArtist;


    private static Datasource instance = new Datasource();

    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            queryAlbumsSortedByArtist = conn.prepareStatement(QUERY_ALBUMS_SORTED_BY_ARTIST);
            queryAlbumsByArtistId = conn.prepareStatement(QUERY_ALBUMS_BY_ARTIST_ID);
            updateArtistName = conn.prepareStatement(UPDATE_ARTIST_NAME);
            updateAlbumName = conn.prepareStatement(UPDATE_ALBUM_NAME);
            updateSongName = conn.prepareStatement(UPDATE_SONG_NAME);
            removeArtist = conn.prepareStatement(REMOVE_ARTIST);
            removeAlbum =  conn.prepareStatement(REMOVE_ALBUM);
            removeSongsOfAlbum =  conn.prepareStatement(REMOVE_SONGS_OF_ALBUM);
            queryArtistById = conn.prepareStatement(QUERY_ARTIST_BY_ID);
            querySongsByAlbumId = conn.prepareStatement(QUERY_SONGS_BY_ALBUM_ID);
            queryAlbumNameById = conn.prepareStatement(QUERY_ALBUM_BY_ALBUM_ID);
            querySongsList = conn.prepareStatement(QUERY_SONGS_LIST);
            querySongsByArtist = conn.prepareStatement(QUERY_SONGS_BY_ARTIST);


            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {

            if(querySongInfoView != null) {
                querySongInfoView.close();
            }

            if(insertIntoArtists != null) {
                insertIntoArtists.close();
            }

            if(insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }

            if(insertIntoSongs !=  null) {
                insertIntoSongs.close();
            }

            if(queryArtist != null) {
                queryArtist.close();
            }
            if(queryArtistById != null) {
                queryArtistById.close();
            }

            if(queryAlbum != null) {
                queryAlbum.close();
            }

            if(queryAlbumsByArtistId != null) {
                queryAlbumsByArtistId.close();
            }

            if(queryAlbumsSortedByArtist != null) {
                queryAlbumsSortedByArtist.close();
            }


            if(updateArtistName != null) {
                updateArtistName.close();
            }

            if(updateSongName != null) {
                updateSongName.close();
            }

            if(updateAlbumName != null) {
                updateAlbumName.close();
            }

            if(removeArtist != null) {
                removeArtist.close();
            }

            if(removeAlbum != null) {
                removeAlbum.close();
            }

            if(removeSongsOfAlbum != null) {
                removeSongsOfAlbum.close();
            }

            if(querySongsByAlbumId != null) {
                querySongsByAlbumId.close();
            }

            if(queryAlbumNameById != null) {
                queryAlbumNameById.close();
            }

            if(querySongsList != null) {
                querySongsList.close();
            }

            if(querySongsByArtist != null) {
                querySongsByArtist.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public List<Artist> queryArtists(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(5);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }

            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Album> queryAlbumsForArtistId(int id) {
        try {
            queryAlbumsByArtistId.setInt(1, id);
            ResultSet results = queryAlbumsByArtistId.executeQuery();

            List<Album> albums = new ArrayList<>();
            while(results.next()) {
                Album album = new Album();
                album.setId(results.getInt(1));
                album.setName(results.getString(2));
                album.setArtistId(id);
                albums.add(album);
            }

            return albums;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Album> queryAlbumsSortedByArtist() {
        try {

            ResultSet results = queryAlbumsSortedByArtist.executeQuery();

            List<Album> albums = new ArrayList<>();
            while(results.next()) {
                Album album = new Album();
                album.setId(results.getInt(1));
                album.setName(results.getString(2));
                album.setArtistId(results.getInt(3));
                albums.add(album);
            }
            return albums;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public ArrayList<Song> querySongsByAlbumId(int id) {
        try {
            querySongsByAlbumId.setInt(1, id);

            ResultSet results = querySongsByAlbumId.executeQuery();
            ArrayList<Song> songs = new ArrayList<>();
            while(results.next()) {
                Song song = new Song();
                song.setId(results.getInt(1));
                song.setTrack(results.getInt(2));
                song.setName(results.getString(3));
                song.setAlbumId(id);

                songs.add(song);
                System.out.println("c "+ song.getName() );
            }
            return songs;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Song> querySongsList() {

        try {
            ResultSet results = querySongsList.executeQuery();
            ArrayList<Song> songs = new ArrayList<>();
            while(results.next()) {
                Song song = new Song();
                song.setId(results.getInt(1));
                song.setTrack(results.getInt(2));
                song.setName(results.getString(3));
                song.setAlbumId(results.getInt(4));

                songs.add(song);

            }
            return songs;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public ArrayList<Song> querySongsListByArtist(int artistId) {

        try {
            querySongsByArtist.setInt(1, artistId);
            ResultSet results = querySongsByArtist.executeQuery();
            ArrayList<Song> songs = new ArrayList<>();
            while(results.next()) {
                Song song = new Song();
                song.setId(results.getInt(1));
                song.setTrack(results.getInt(2));
                song.setName(results.getString(3));
                song.setAlbumId(results.getInt(4));

                songs.add(song);
            }
            return songs;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }




    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {

        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL statement = " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<String> albums = new ArrayList<>();
            while (results.next()) {
                albums.add(results.getString(1));
            }

            return albums;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public void querySongsMetadata() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                System.out.format("Column %d in the songs table is names %s\n",
                        i, meta.getColumnName(i));
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }

    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count FROM " + table;
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            int count = results.getInt("count");

            System.out.format("Count = %d\n", count);
            return count;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    }

    public boolean createViewForSongArtists() {

        try (Statement statement = conn.createStatement()) {

            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;

        } catch (SQLException e) {
            System.out.println("Create View failed: " + e.getMessage());
            return false;
        }
    }

    public int insertArtist(String name) throws SQLException {

        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the artist
            insertIntoArtists.setString(1, name);
            int affectedRows = insertIntoArtists.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert artist!");
            }

            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }

    public int removeArtist(String name) throws SQLException {

        int artistId = getArtistId(name);
        int affectedRecords = 0;
        System.out.println("remove data :" + artistId);
        if (artistId > 0) {

            try { // implement: autocomit and rollback : vedi insertsong
                removeAlbum.setInt(1,artistId );
                affectedRecords = removeAlbum.executeUpdate();
                System.out.println("remove data 1 :" + affectedRecords);

                } catch (SQLException e) {
                System.out.println("Remove failed: " + e.getMessage());
                return 0;
            }

                try {
                    removeArtist.setInt(1, artistId);
                    affectedRecords += removeArtist.executeUpdate();
                    System.out.println("remove data 2 :" + affectedRecords);
                    return affectedRecords ;

                } catch (SQLException e) {
                    System.out.println("Remove failed: " + e.getMessage());
                    return 1;
                }


       }    else {
            System.out.println("Didn't get Artist " + name +" id: " + artistId);
            return 0;
        }
    }

    public int removeAlbum(int id ) throws SQLException {
        int affectedRecords = 0;
        System.out.println("remove data :" + id);
        if (id > 0) {

            try { // implement: autocomit and rollback : vedi insertsong
                conn.setAutoCommit(false);

                removeSongsOfAlbum.setInt(1, id);
                affectedRecords = removeSongsOfAlbum.executeUpdate();
                System.out.println("remove data 1 :" + affectedRecords);

            } catch (SQLException e) {
                System.out.println("Remove failed: " + e.getMessage());

                try {
                    System.out.println("Performing rollback");
                    conn.rollback();
                } catch (SQLException e2) {
                    System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                }
                return 0;
            }

            try {
                removeAlbum.setInt(1, id);
                affectedRecords += removeAlbum.executeUpdate();
                System.out.println("remove data 2 :" + affectedRecords);
                return affectedRecords;

            } catch (SQLException e) {
                System.out.println("Remove failed: " + e.getMessage());

                try {
                    System.out.println("Performing rollback");
                    conn.rollback();
                } catch (SQLException e2) {
                    System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
                }

                return 1;

            } finally {
                try {
                    System.out.println("Resetting default commit behavior");
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Couldn't reset auto-commit! " + e.getMessage());
                }
            }
        } else {
            System.out.println("Didn't get Album id: " + id);
            return 0;
        }
    }


    public int getArtistId (String name)throws SQLException {
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if(results.next()) return results.getInt(1);
        else return -1;
    }

    // to test
    public  String getArtistName(int id)throws SQLException {
        queryArtistById.setInt(1, id);
        ResultSet results = queryArtistById.executeQuery();
        if(results.next()) return results.getString(1);
        else return null;
    }

    public  String getAlbumName(int id)throws SQLException {
        queryAlbumNameById.setInt(1, id);
        ResultSet results = queryAlbumNameById.executeQuery();
        if(results.next()) return results.getString(1);
        else return null;
    }

    public int insertAlbum(String name, int artistId) throws SQLException {

        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the album
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int affectedRows = insertIntoAlbums.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert album!");
            }

            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for album");
            }
        }
    }

    public boolean updateArtistName(int id, String newName) {
        try {
            updateArtistName.setString(1, newName);
            updateArtistName.setInt(2, id);
            int affectedRecords = updateArtistName.executeUpdate();

            return affectedRecords == 1;

        } catch(SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }


    public boolean updateAlbumName(int id, String newName) {
        try {
            updateAlbumName.setString(1, newName);
            updateAlbumName.setInt(2, id);
            int affectedRecords = updateAlbumName.executeUpdate();

            return affectedRecords == 1;

        } catch(SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }


    public boolean updateSongName(int id, String newName) {
        try {
            updateSongName.setString(1, newName);
            updateSongName.setInt(2, id);
            int affectedRecords = updateSongName.executeUpdate();

            return affectedRecords == 1;

        } catch(SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    public void insertSong(int track, String title, int albumId) {

        try {
            conn.setAutoCommit(false);

            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumId);
            int affectedRows = insertIntoSongs.executeUpdate();
            if(affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The song insert failed");
            }

        } catch(Exception e) {
            System.out.println("Insert song exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }

    }

    public boolean resetSongs (int albumId){

        try {
            removeSongsOfAlbum.setInt(1, albumId);
            removeSongsOfAlbum.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }

    }

}















