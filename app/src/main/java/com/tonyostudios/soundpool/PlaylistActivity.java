package com.tonyostudios.soundpool;

import android.app.Activity;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;


public class PlaylistActivity extends Activity implements Ambience.AmbientListener {

    private ListView playlistListView;
    private TextView titleTextView;

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

        mAdapter = new PlaylistAdapter(this,android.R.layout.simple_list_item_1,new ArrayList<String>());
        playlistListView.setAdapter(mAdapter);

        playlistListView.setOnItemClickListener(mListClick);

        setUpPlaylist();

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

                    mAdapter.addAll(playlistUris);

                    Ambience.activeInstance()
                            .setPlaylistTo(getPlaylist());


                }

            }
        });

    }

    private ArrayList<AmbientTrack> getPlaylist() {

        ArrayList<AmbientTrack> playlist = new ArrayList<AmbientTrack>();

        for(int x = 0; x < mAdapter.getCount(); x++)
        {
            AmbientTrack track = AmbientTrack.newInstance();


            Uri uri = Uri.parse(mAdapter.getItem(x));

            track.setAudioDownloadUri(uri);
            track.setAudioUri(uri);

            playlist.add(track);

        }

        return playlist;

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
