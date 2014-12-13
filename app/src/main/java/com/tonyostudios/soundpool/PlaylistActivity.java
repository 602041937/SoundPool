package com.tonyostudios.soundpool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.tonyostudios.ambience.Ambience;
import com.tonyostudios.ambience.AmbientTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;


public class PlaylistActivity extends Activity implements Ambience.AmbientListener {

    private ListView playlistListView;
    private TextView titleTextView;
    private static Context mContext;

    private final static String PLAYLIST_ID = "WMi3fNDwsm";

    private ParseObject mPool;
    private PlaylistAdapter mAdapter;

    private AmbientTrack currentTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlistListView = (ListView) findViewById(R.id.playlist_listView);
        titleTextView = (TextView) findViewById(R.id.playlist_name);



        playlistListView.setOnItemClickListener(mListClick);

        setUpPlaylist();

        mContext = this;

        Ambience.turnOn(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

       Ambience.activeInstance()
               .listenForUpdatesWith(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Ambience.stopListeningForUpdates();
        Ambience.turnOff();
    }

    private AdapterView.OnItemClickListener mListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Ambience.activeInstance()
                   .PlayFromPosition(position);

        }
    };

    private void setUpPlaylist() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("pool");

        query.getInBackground(PLAYLIST_ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if(e == null)
                {

                    mPool = parseObject;
                    String playlistName = parseObject.getString("playlistName");
                    titleTextView.setText(playlistName);

                    List<String> playlistUris = parseObject.getList("playlistUris");

                    new ProcessAmbientTracks().execute(playlistUris);



                }

            }
        });

    }

    private void getPlaylist() {

        new ProcessAmbientTracks().execute();

    }

    private class ProcessAmbientTracks extends AsyncTask<List<String>,Void,ArrayList<AmbientTrack>>
    {
        @Override
        protected ArrayList<AmbientTrack> doInBackground(List<String>... params) {

            List<String> uris = params[0];

            ArrayList<AmbientTrack> playlist = new ArrayList<AmbientTrack>();

            for(int x = 0; x < uris.size(); x++)
            {
                AmbientTrack track = AmbientTrack.newInstance();


                Uri uri = Uri.parse(uris.get(x));

                track.setAudioDownloadUri(uri);
                track.setAudioUri(uri);

                FFmpegMediaMetadataRetriever ffmr = new FFmpegMediaMetadataRetriever();
                ffmr.setDataSource(uris.get(x));// .setDataSource(mContext,uri);

                String album = ffmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);

               String artist = ffmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
               String date = ffmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DATE);
               String genre =  ffmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_GENRE);
               String title = ffmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);

                try
                {

                   File cache = mContext.getCacheDir();

                    File albumFile = new File(cache.getAbsolutePath() + File.separatorChar + uri.getLastPathSegment());

                    Bitmap b = ffmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds
                    byte [] artwork = ffmr.getEmbeddedPicture();

                    FileOutputStream stream = new FileOutputStream(albumFile);
                    try {
                        stream.write(artwork);
                    } finally {
                        stream.close();
                    }


                    track.setAlbumImageUri(Uri.parse(albumFile.getAbsolutePath()));

                }catch (Exception e)
                {
                    e.printStackTrace();
                }



                ffmr.release();

                ArrayList<String> genres = new ArrayList<String>();

                genres.add(genre);

                track.setName(title)
                        .setArtistName(artist)
                        .setReleaseDate(date)
                        .setAlbumName(album)
                        .setGenres(genres);


                playlist.add(track);
            }

            return playlist;
        }

        @Override
        protected void onPostExecute(ArrayList<AmbientTrack> tracks) {
            super.onPostExecute(tracks);


            mAdapter = new PlaylistAdapter(mContext,R.layout.track,tracks);
            playlistListView.setAdapter(mAdapter);

            Ambience.activeInstance()
                    .setPlaylistTo(tracks);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void ambienceIsPreppingTrack() {

    }

    @Override
    public void ambienceTrackDuration(int time) {

    }

    @Override
    public void ambiencePlayingTrack(AmbientTrack track) {

    }

    @Override
    public void ambienceTrackCurrentProgress(int time) {

    }

    @Override
    public void ambienceTrackIsPlaying() {

    }

    @Override
    public void ambienceTrackIsPaused() {

    }

    @Override
    public void ambienceTrackHasStopped() {

    }

    @Override
    public void ambiencePlaylistCompleted() {

    }

    @Override
    public void ambienceErrorOccurred() {

    }

    @Override
    public void ambienceServiceStarted(Ambience activeInstance) {

    }

    @Override
    public void ambienceServiceStopped(Ambience activeInstance) {

    }
}
