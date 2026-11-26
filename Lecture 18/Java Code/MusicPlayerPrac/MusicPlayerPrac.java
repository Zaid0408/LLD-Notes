import java.util.*;

/*
 * LLD Problem: Design a Music Player
 *
 * Functional Requirements (FRs):
 *
 * FR1. Music Library
 *      - Maintain an in-memory collection of songs.
 *      - Each song should have an ID, title, artist, album name,
 *        duration and source information.
 *      - Support adding, removing, retrieving and listing songs.
 *
 * FR2. Search
 *      - Allow searching songs by title or artist.
 *      - Support basic string matching only.
 *
 * FR3. Playlist Management
 *      - Allow users to create playlists.
 *      - Add and remove songs from playlists.
 *      - View playlist contents.
 *      - Prevent duplicate songs within a playlist.
 *
 * FR4. Offline Downloads
 *      - Allow downloading individual songs and entire playlists.
 *      - Maintain a special playlist called "Offline Downloads".
 *      - Prevent duplicate entries in offline downloads.
 *      - Simulate the download operation through console output.
 *
 * FR5. Music Playback
 *      - Allow playing an individual song or a playlist.
 *      - Support Play, Pause, Resume, Stop, Next and Previous.
 *      - Maintain a playback queue and current song index.
 *      - Ensure only one song is actively playing at a time.
 *
 * FR6. Multiple Playback Sources
 *      - Support local playback for downloaded songs.
 *      - Support streaming playback for songs that are not downloaded.
 *      - Select the appropriate playback strategy automatically.
 *      - Simulate playback through console output.
 *
 * FR7. Playback Analytics
 *      - Implement an Analytics Service using the Observer pattern.
 *      - Automatically record playback events such as:
 *        SONG_PLAYED, SONG_PAUSED, SONG_RESUMED,
 *        SONG_SKIPPED and SONG_STOPPED.
 *
 * FR8. Offline Playback Queue
 *      - Implement a separate queue for offline playback.
 *      - Allow playback of downloaded songs through this queue.
 *      - Ensure only downloaded songs are included in the offline queue.
 *
 *
 * Non-Functional Requirements (NFRs):
 *
 * NFR1. Extensibility
 *       - New playback sources should be supported without modifying
 *         the core MusicPlayer implementation.
 *
 * NFR2. Data Consistency
 *       - Only one song should be actively playing at a time.
 *       - Playlists and offline downloads should not contain duplicates.
 *       - Playback queue indexes and current song should remain consistent.
 *
 * NFR3. Maintainability
 *       - Separate responsibilities across entities and services.
 *       - Use appropriate design patterns and SOLID principles.
 *
 *
 * Constraints:
 *
 * - Single-user application.
 * - Java-based implementation.
 * - In-memory storage; no database.
 * - Console-based simulation; no actual audio playback or downloading.
 * - No login, registration, recommendations, subscriptions,
 *   equalizer or lyrics synchronization.
 */

public class MusicPlayerPrac {
    long songId=0,playListId=0;
    enum PlaybackStatus{
        PLAYING,PAUSED,STOPPED;
    }
    // Entities
    class Song{
        long id;
        String title,artist,albumName,duration,info;
        boolean downloaded;
        public Song(String title,String artist,String albumName,String duration,String info)
        {
            this.id=songId++;
            this.title=title;this.artist=artist;this.downloaded=false;
            this.albumName=albumName;this.duration=duration;this.info=info;
        }
        long getId(){ return this.id;}
        boolean isDownloaded() { return  this.downloaded;}
    }

    class PlayList{
        String name;
        long id;
        List<Song> songs;
        public PlayList(String name)
        {
            this.id=playListId++;
            songs=new ArrayList<>();
            this.name=name;
        }
        void add(Song song)
        {
            if (!songs.contains(song)) {
                songs.add(song);
            }
        }
        void remove(Song song)
        {
            songs.remove(song);
        }
        void viewSongs()
        {
            System.out.println("Printing all Songs in PlayList: "+name);
            for(Song son:songs)
            {
                System.out.println("Song Name: "+ son.title+ " Artist: "+ son.artist+ "Album: "+ son.albumName+ "Duration: "+ son.duration);
            }
        }
        long getId(){ return this.id;}

    }

    class MusicLibrary{
        Map<Long, PlayList> playlists;
        Map<Long,Song> songs; // used for basic search fo all songs
        public MusicLibrary()
        {
            playlists=new HashMap<>();
            songs=new HashMap<>();
        }
        void addSong(Song song)
        {
            long id=song.getId();
            songs.put(id,song);
        }
        void removeSong(Song song)
        {
            long id=song.getId();
            songs.remove(id);
        }
        void addPlayList(PlayList list)
        {
            long id=list.getId();
            playlists.put(id,list);
        }
        void removePlayList(PlayList playlist)
        {
            long id=playlist.getId();
            playlists.remove(id);
        }
    }


    class MusicPlayer{
        private PlaybackController playbackController;
        private List<Song> songList;
        private Song currentSong;
        private int currIndex;
        private PlaybackStatus playbackStatus;
        private boolean isOnline;
        private PlaybackStrategyFactory playbackStrategyFactory;

        public MusicPlayer(Song currentSong)
        {
            this.currentSong=currentSong;this.currIndex=0;this.isOnline=true;
            this.songList=new ArrayList<>();this.playbackStatus=PlaybackStatus.STOPPED;
            this.playbackStrategyFactory = new PlaybackStrategyFactory();
            this.playbackController=new PlaybackController(new Play(playbackStrategyFactory,isOnline), new Pause());
        }
        String getSongTitle()
        {
            return currentSong.title;
        }
        PlaybackStatus getStatus() { return this.playbackStatus;}
        void setStatus(PlaybackStatus pl) { this.playbackStatus=pl;}
        void playSong()
        {
            if (currentSong == null) {
                System.out.println("No SOng Selected for the music player");
                return;
            }
            if(playbackStatus==PlaybackStatus.PLAYING)
            {
                System.out.println("Song is already playing , Continuing..,");
                return;
            }
            setStatus(PlaybackStatus.PLAYING);
            playbackController.playSong(currentSong);
        }
        void pauseSong()
        {
            if(playbackStatus!=PlaybackStatus.PLAYING) // change from checking ==pause to !=playing
            // because what if there are no songs playing and we hit this 
            {
                System.out.println("Song is already paused , Cannot pause..");
                return;
            }
            setStatus(PlaybackStatus.PAUSED);
            playbackController.pauseSong(currentSong);
        }
        void removeSong(Song song, Optional<Integer> idx) // same method can be used to remove a song to the end or at a particular idx
        {
            int removeIndex;
            if (idx.isPresent()) {
                removeIndex = idx.get();
            } else {
                removeIndex = songList.indexOf(song);
            }
            if (removeIndex < 0 || removeIndex >= songList.size()) {
                System.out.println("Song not found!");
                return;
            }
            boolean removingCurrentSong = (removeIndex == currIndex);
            songList.remove(removeIndex);
            if (removingCurrentSong) {
                currentSong = null;
                playbackStatus = PlaybackStatus.STOPPED;
                currIndex = Math.min(currIndex, songList.size() - 1);
            }
            else if (removeIndex < currIndex) {
                currIndex--;
            }
        }
        void addSong(Song song, Optional<Integer> idx) { // same method can be used to add a song to the end or at a particular idx
            int insertIndex = idx.orElse(songList.size());
            if (insertIndex < 0 || insertIndex > songList.size()) {
                throw new IndexOutOfBoundsException("Invalid insertion index");
            }
            songList.add(insertIndex, song);
            // Insertion before current song shifts its index.
            if (insertIndex <= currIndex && currentSong != null) {
                currIndex++;
            }
        }
        void listSongs()
        {
            System.out.println("The songs in this Music Player are: ");
            for(Song song: songList)
            {
                System.out.println("Song Name: "+ song.title+ " Artist: "+ song.artist+ "Album: "+ song.albumName+ "Duration: "+ song.duration);
            }

        }
        void next()
        {
            if(songList.size()==0)
            {
                System.out.println("No songs in list!");
                return;
            }
            if(currIndex+1>=songList.size())
            {
                System.out.println("End of the List No more songs. Please add new song to the playlist to listen to new songs...");
                return ;
            }

            currIndex++;
            currentSong=songList.get(currIndex);
            playSong();
        }
        void previous()
        {
            if(songList.size()==0)
            {
                System.out.println("No songs in list!");
                return;
            }
            if(currIndex-1==-1)
            {
                System.out.println("End of the List No more songs. Please add new song to the playlist to listen to new songs...");
                return ;
            }
            currIndex--;
            currentSong=songList.get(currIndex);
            playSong();
        }
    }

    // strategy for Online or offline playing of audio 

    interface PlaybackStrategy{
        void play(Song song);
    }

    class StreamingPlayBack implements PlaybackStrategy{
        @Override 
        public void play(Song song)
        {
            System.out.println("[StreamingPlayBack]: Song Name: "+ song.title+ " Artist: "+ song.artist+ " Duration: "+ song.duration);
        }
    }
    class OfflinePlayBack implements PlaybackStrategy{
        @Override 
        public void play(Song song)
        {
            System.out.println("[OfflinePlayBack]: Song Name: "+ song.title+ " Artist: "+ song.artist+ " Duration: "+ song.duration);
        }
    }

    class PlaybackStrategyFactory{
        public PlaybackStrategy getStrategy(Song song, boolean isOnline) // to override the strategy if we need to change to offline and online
        {
            if(isOnline)
            {
                return new StreamingPlayBack();
            }
            else if(!isOnline && song.isDownloaded())
            {
                return new OfflinePlayBack();
            }
            throw new IllegalStateException("Song is not downloaded and device is offline: " + song.title);
        }

    }
    

    // command pattern for Pause , Play 
    class Play{ // this is a recievr for the play command. But at the same time is a clinet for the strategy pattern
        private PlaybackStrategyFactory factory;
        private boolean isOnline;
        // PlaybackStrategyFactory decides the which Strategy to be followed while streaming , online or offline 
        // Please check all the classes to get to know the ful picture , Strategy is used in the Play command reciever only . 
        public Play(PlaybackStrategyFactory factory, boolean isOnline) {
            this.factory = factory;
            this.isOnline = isOnline;
        }
        void execute(Song song) {
            System.out.println("Playing song ...");
            PlaybackStrategy strategy =factory.getStrategy(song, isOnline);
            strategy.play(song);
        }   
    }

    class Pause{
        void execute(Song son)
        {
            System.out.println("Pausing song ...");
            System.out.println("Song Name: "+ son.title);
        }
    }
    interface PlaybackCommand
    {
        void execute(Song song);
    }

    class PlayCommand implements PlaybackCommand{ // boolean to check if song is paused is handled by Music player
        private Play play;
        public PlayCommand(Play p)
        {
            this.play=p;
        }

        public void execute(Song song)
        {
            play.execute(song);
        }
    }

    class PauseCommand implements PlaybackCommand{
        private Pause pause;
        public PauseCommand(Pause p)
        {
            this.pause=p;
        }

        public void execute(Song song)
        {
            pause.execute(song);
        }
    }

    class PlaybackController{
        private PlaybackCommand play,pause;

        public PlaybackController(Play play,Pause pause)
        {
            this.play=new PlayCommand(play);
            this.pause=new PauseCommand(pause);
        }

        void playSong(Song song)
        {
            play.execute(song);
        }
        void pauseSong(Song song)
        {
            pause.execute(song);
        }
    }


    public static void main(String[] args)
    {
        MusicPlayerPrac app = new MusicPlayerPrac();

        // 1. Create songs
        Song s1 = app.new Song(
            "Blinding Lights", "The Weeknd",
            "After Hours", "3:20", ""
        );

        Song s2 = app.new Song(
            "Starboy", "The Weeknd",
            "Starboy", "3:50", ""
        );

        Song s3 = app.new Song(
            "Perfect", "Ed Sheeran",
            "Divide", "4:23", ""
        );

        Song s4 = app.new Song("We are the people", "One D", 
        "Sunshine", "3:34", "");

        // 2. Create Music Library
        MusicLibrary library = app.new MusicLibrary();

        library.addSong(s1);
        library.addSong(s2);
        library.addSong(s3);
        library.addSong(s4);

        // 3. Create playlist
        PlayList playlist = app.new PlayList("My Favourites");

        playlist.add(s1);
        playlist.add(s2);
        playlist.add(s3);

        library.addPlayList(playlist);

        // 4. View playlist
        playlist.viewSongs();

        // 5. Search song (basic search)
        System.out.println("\nSearching for The Weeknd:");

        for (Song song : library.songs.values()) {
            if (song.artist.toLowerCase().contains("the weeknd")) {
                System.out.println(song.title);
            }
        }

        // 6. Create Music Player
        MusicPlayer player = app.new MusicPlayer(s2);

        // Add playlist songs to playback queue
        player.addSong(s1, Optional.empty());
        player.addSong(s2, Optional.empty());
        player.addSong(s3, Optional.empty());

        // 7. Start playback
        player.playSong();

        // 8. Pause
        player.pauseSong();

        // 9. Resume
        player.playSong();

        // 10. Previous song
        player.previous();

        // 11. Next song
        player.next();

        // 12. View player queue
        player.listSongs();
    }
}
