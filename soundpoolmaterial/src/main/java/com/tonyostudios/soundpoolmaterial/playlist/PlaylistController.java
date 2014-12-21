package com.tonyostudios.soundpoolmaterial.playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 12/21/14.
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
    private List<RefresherListener> mCallbacks = new ArrayList<RefresherListener>();


    /**
     * Private constructor
     */
    private PlaylistController()
    {
        super();
    }


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


    public void addCallbackListener(RefresherListener callback)
    {
        if(callback == null)
        {
            throw new NullPointerException("refresher callback cannot be null");
        }


        mCallbacks.add(callback);

    }

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

                listener.refreshedPlaylistData();
            }
        }

    }


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
        public abstract void refreshedPlaylistData();
    }
}
