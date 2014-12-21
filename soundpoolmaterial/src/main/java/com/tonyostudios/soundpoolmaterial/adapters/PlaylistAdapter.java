package com.tonyostudios.soundpoolmaterial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.tonyostudios.soundpoolmaterial.models.Playlist;

import java.util.List;

/**
 * Created by tonyostudios on 12/21/14.
 */
public class PlaylistAdapter extends ArrayAdapter<Playlist> {

    private Context mContext;
    private int mResource;
    private List<Playlist> mPlaylist;

    public PlaylistAdapter(Context context, int resource, List<Playlist> playlist) {
        super(context, resource, playlist);

        mContext = context;
        mResource = resource;
        mPlaylist = playlist;

    }

    @Override
    public int getCount() {
        super.getCount();

        if(mPlaylist == null)
        {
            return 0;
        }

        return mPlaylist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);

        View rootView = convertView;

        if(rootView == null)
        {
            //Inflate new view
            rootView = LayoutInflater.from(mContext)
                                     .inflate(mResource,parent,false);

            rootView.setTag(new ViewHolder(rootView));
        }


        ViewHolder viewHolder = (ViewHolder) rootView.getTag();
        Playlist playlist = mPlaylist.get(position);


        // Attach data to view here



        return rootView;
    }

    private void swapPlaylist(List<Playlist> playlist)
    {
        if(playlist == null)
        {
            throw new NullPointerException("cannot swap current playlist with null");
        }

        mPlaylist.clear();
        mPlaylist = null;
        mPlaylist = playlist;
        notifyDataSetChanged();
    }


    public static class ViewHolder
    {
        private View view;

        public ViewHolder(View view)
        {
            this.view  = view;
        }


        public View getView() {
            return view;
        }
    }

}
