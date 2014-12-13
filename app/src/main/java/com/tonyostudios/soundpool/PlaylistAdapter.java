package com.tonyostudios.soundpool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tonyostudios.ambience.AmbientTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 12/13/14.
 */
public class PlaylistAdapter extends ArrayAdapter<AmbientTrack> {

    private Context mContext;
    private int mResource;
    private ArrayList<AmbientTrack> mData;

    public PlaylistAdapter(Context context, int resource, ArrayList<AmbientTrack> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource = resource;
        mData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;

        if(convertView == null)
        {
            rootView = LayoutInflater.from(mContext).inflate(mResource,parent,false);
            rootView.setTag(new ViewHolder(rootView));
        }


        ViewHolder holder = (ViewHolder) rootView.getTag();

        AmbientTrack track = mData.get(position);

        holder.setTitle(track.getName());
        holder.setAlbum(track.getAlbumName());


        return rootView;


    }

    @Override
    public int getCount() {
        return mData.size();
    }


    private static class ViewHolder
    {
        private View view;
        private TextView mTitle;
        private TextView mAlbum;

        public ViewHolder(View view)
        {
            this.view = view;
            mTitle = (TextView) view.findViewById(R.id.track_name);
            mAlbum = (TextView) view.findViewById(R.id.track_album);
        }

        public View getView() {
            return view;
        }

        public TextView getmTitle() {
            return mTitle;
        }

        public TextView getmAlbum() {
            return mAlbum;
        }

        public void setTitle(String title)
        {
            getmTitle().setText(title);
        }

        public  void setAlbum(String album)
        {
            getmAlbum().setText(album);
        }
    }
}
