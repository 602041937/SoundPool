package com.tonyostudios.soundpoolmaterial.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyostudios on 12/21/14.
 */
public class Playlist implements Parcelable {

    private String name = "";
    private int size = 0;
    private int followerCount = 0;
    private String creator = "";

    private List<String> tracks = new ArrayList<String>();


    private Playlist()
    {
        super();
    }

    public static Playlist newInstance()
    {
        return new Playlist();
    }


    private static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel parcel) {

            Playlist playlist = Playlist.newInstance();


            playlist.setName(parcel.readString())
                    .setSize(parcel.readInt())
                    .setFollowerCount(parcel.readInt())
                    .setCreator(parcel.readString());


            parcel.readStringList(playlist.getTracks());



            return playlist;
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

            parcel.writeString(name);
            parcel.writeInt(size);
            parcel.writeInt(followerCount);
            parcel.writeString(creator);
            parcel.writeStringList(tracks);
    }


    public String getName() {
        return name;
    }

    public Playlist setName(String name) {

        if(name == null)
        {
            throw new NullPointerException("Name cannot be null");
        }

        this.name = name;


        return this;
    }

    public int getSize() {
        return size;
    }

    public Playlist setSize(int size) {
        this.size = size;

        return this;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public Playlist setFollowerCount(int followerCount) {
        this.followerCount = followerCount;

        return this;
    }

    public String getCreator() {
        return creator;
    }

    public Playlist setCreator(String creator) {

        if(creator == null)
        {
            throw new NullPointerException("creator cannot be null");
        }


        this.creator = creator;

        return this;
    }

    public List<String> getTracks() {
        return tracks;
    }

    public Playlist setTracks(List<String> tracks) {

        if(tracks == null)
        {
            throw new NullPointerException("tracks list cannot be null");
        }

        for(int x = 0; x < tracks.size(); x++)
        {
            if(tracks.get(x) == null)
            {
                throw new NullPointerException("track cannot be null");
            }
        }


        this.tracks = tracks;


        return this;
    }
}
