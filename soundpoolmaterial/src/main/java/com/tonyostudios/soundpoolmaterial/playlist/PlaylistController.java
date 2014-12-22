package com.tonyostudios.soundpoolmaterial.playlist;

import android.os.Handler;

import com.tonyostudios.soundpoolmaterial.models.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyostudios on 12/21/14.
 */
public class PlaylistController {

    /**
     * Holds the PlaylistController static instance
     */
    private static PlaylistController mPlaylistController;


    /**
     * Holds all the callback methods used to pass data to different parts of the
     * application to update playlistViews;
     */
    private ArrayList<RefresherListener> mCallbacks = new ArrayList<RefresherListener>();


    /**
     * Holds a list of playlist items associated with the user
     */
    private List<Playlist> mPlaylist = new ArrayList<Playlist>();


    /**
     * Private constructor
     */
    private PlaylistController()
    {
        super();
    }


    /**
     * Handler used to check for playlist updates
     */
    private Handler mHandler = new Handler();


    /**
     * Static variable that holds ten mins in millisecond.
     * This variable is used to update the playlist ever ten mins.
     */
    private static final int TEN_MINS = 600000;

    /**
     * Method returns single instance of the playlist controller;
     * @return instance of the playlist controller
     */
    public static PlaylistController getInstance()
    {
        if(mPlaylistController == null)
        {
            mPlaylistController = new PlaylistController();
        }


        return mPlaylistController;
    }

    /**
     * Method used to add a refresherListener. Listener
     * will be alerted when the playlist data is refreshed
     * @param callback A refresherListener callback
     */
    public void addCallbackListener(RefresherListener callback)
    {
        if(callback == null)
        {
            throw new NullPointerException("refresher callback cannot be null");
        }


        mCallbacks.add(callback);

    }

    /**
     * Method used to remove a refresherListener callback
     * @param callback A refresherListener callback
     */
    public void removeCallbackListener(RefresherListener callback)
    {
        if(callback == null)
        {
            return;
        }

        mCallbacks.remove(callback);
    }

    /**
     * Method used to alert the listeners that the playlist
     * has been updated with new data.
     */
    private void updateListener()
    {

        if(mCallbacks != null)
        {
            for(int x = 0; x < mCallbacks.size(); x++)
            {
                RefresherListener listener = mCallbacks.get(x);

                listener.refreshedPlaylistData(mPlaylist); // update each callback
            }
        }

    }


    /**
     * Runnable used to check for playlist updates
     */
    private Runnable mCountDownRunnable = new Runnable() {
        @Override
        public void run() {

            //TODO FETCH UPDATES HERE.

        }
    };


    /**
     * Method used to clear all resources used by the PlaylistController
     */
    public void release()
    {

        if(mCallbacks != null)
        {
            mCallbacks.clear();
            mCallbacks = null;
        }


        if(mPlaylistController != null)
        {
            mPlaylistController = null;
        }

        if(mPlaylist != null)
        {
            mPlaylist.clear();
            mPlaylist = null;
        }

        if(mHandler != null)
        {
            mHandler.removeCallbacks(mCountDownRunnable);
            mHandler = null;
        }

        if(mCountDownRunnable != null)
        {
            mCountDownRunnable = null;
        }

    }



    /**
     * Listener used to update views throughout
     * the application
     */
    public static interface RefresherListener
    {
        /**
         * Method called to pass refresh playlist data
         * to application
         */
        public abstract void refreshedPlaylistData(List<Playlist> playlist);
    }
}
