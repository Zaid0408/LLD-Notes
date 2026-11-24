import java.util.*;
public class MusicPlayerPrac {
    long songId=0,playListId=0;
    // Entities
    class Song{
        long id;
        String title,artist,albumName,duration,info;

        public Song(String title,String artist,String albumName,String duration,String info)
        {
            this.id=songId++;
            this.title=title;this.artist=artist;
            this.albumName=albumName;this.duration=duration;this.info=info;
        }
        long getId(){ return this.id;}
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
            songs.add(song);
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
        private List<Song> queue;
        private Song currentSong;
        private boolean isPaused;
        private int currIndex;

        public MusicPlayer(Song currentSong)
        {
            this.currentSong=currentSong; this.isPaused=false;this.currIndex=0;
            this.queue=new ArrayList<>();// faster then LinkedList
            this.playbackController=new PlaybackController(new Play(), new Pause());
        }
        boolean isPaused() {
            return isPaused;
        }
        String getSongTitle()
        {
            return currentSong.title;
        }
        void playSong()
        {
            playbackController.playSong(currentSong);
        }
        void pauseSong()
        {
            if(isPaused)
            {
                System.out.println("Song is already paused , Cannot pause..");
                return;
            }
            playbackController.pauseSong(currentSong);
        }
        void removeSong(Song song, Optional<Integer> idx) // same method can be used to remove a song to the end or at a particular idx
        {
            if (idx.isPresent()){
                queue.remove((int) idx.get());
                if((int)idx.get() < currIndex)
                    currIndex--;
            }
            else
                queue.remove(song);
        }
        void addSong(Song song, Optional<Integer> idx) // same method can be used to add a song to the end or at a particular idx
        {
            if (idx.isPresent()){
                queue.add((int) idx.get(),song);
                if((int)idx.get() < currIndex)
                    currIndex++;
            }
            else
                queue.add(song);
        }
        void listSongs()
        {
            System.out.println("The songs in this Music Player are: ");
            for(Song song: queue)
            {
                System.out.println("Song Name: "+ song.title+ " Artist: "+ song.artist+ "Album: "+ song.albumName+ "Duration: "+ song.duration);
            }

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
        private PlaybackStrategy playbackStrategy;

        public PlaybackStrategyFactory(PlaybackStrategy p)
        {
            this.playbackStrategy=p;
        }
        public void setStrategy(PlaybackStrategy p) // to override the strategy if we need to change to offline and online
        {
            this.playbackStrategy=p;
        }

        public void play(Song song) // the appropriate strategy will play the song 
        {
            playbackStrategy.play(song);
        }

    }
    

    // command pattern for Pause , Play 
    class Play{
        void execute(Song son)
        {
            System.out.println("Playing song ...");
            System.out.println("Song Name: "+ son.title+ " Artist: "+ son.artist+ " Duration: "+ son.duration);
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

    class Next{
        
    }

    public static void main(String[] args)
    {
        
    }
}
