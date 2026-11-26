import java.util.*;
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
        PlaybackStatus playbackStatus;
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
        private List<Song> songList;
        private Song currentSong;
        private int currIndex;
        private PlaybackStatus playbackStatus;
        private boolean isOnline;
        private PlaybackStrategyFactory playbackStrategyFactory;

        public MusicPlayer(Song currentSong)
        {
            this.currentSong=currentSong;this.currIndex=0;this.isOnline=true;
            this.songList=new ArrayList<>();
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
            if(playbackStatus==PlaybackStatus.PAUSED)
            {
                System.out.println("Song is already paused , Cannot pause..");
                return;
            }
            setStatus(PlaybackStatus.PAUSED);
            playbackController.pauseSong(currentSong);
        }
        void removeSong(Song song, Optional<Integer> idx) // same method can be used to remove a song to the end or at a particular idx
        {
            if (idx.isPresent()){
                songList.remove((int) idx.get());
                if((int)idx.get() < currIndex)
                    currIndex--;
            }
            else
                songList.remove(song);
        }
        void addSong(Song song, Optional<Integer> idx) // same method can be used to add a song to the end or at a particular idx
        {
            if (idx.isPresent()){
                songList.add((int) idx.get(),song);
                if((int)idx.get() < currIndex)
                    currIndex++;
            }
            else
                songList.add(song);
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
            if(currIndex+1==songList.size())
            {
                System.out.println("End of the List No more songs. Please add new song to the playlist to listen to new songs...");
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
            else if(song.isDownloaded())
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
        
    }
}
