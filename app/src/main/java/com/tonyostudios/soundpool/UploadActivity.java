package com.tonyostudios.soundpool;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileInputStream;


public class UploadActivity extends Activity {

    private static final String TAG = "UploadActivity";

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    // code

    private ParseObject mParsePool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MusicUploadFragment())
                    .commit();
        }


        Parse.initialize(this, "IhBjPwNMtFZcStsg2YUzlzYMrPQWFhxF5Nm6ckJ7", "cnBqiiiuyzC5aVhYsyprfblpfIQlhIwNFI2IbIPe");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
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



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MusicUploadFragment extends Fragment {

        public MusicUploadFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_upload, container, false);

            Button uploadButton = (Button) rootView.findViewById(R.id.uploadButton);

            uploadButton.setOnClickListener(MusicUploadClick);

            return rootView;
        }


        private View.OnClickListener MusicUploadClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent target = FileUtils.createGetContentIntent();
                // Create the chooser Intent
                Intent intent = Intent.createChooser(
                        target, "Select A File");
                try {
                   startActivityForResult(intent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    // The reason for the existence of aFileChooser
                }

            }
        };

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // If the file selection was successful
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            // Get the URI of the selected file
                            final Uri uri = data.getData();
                            Log.i(TAG, "Uri = " + uri.toString());
                            try {
                                // Get the file path from the URI
                                final String path = FileUtils.getPath(getActivity(), uri);
                                Toast.makeText(getActivity(),
                                        "File Selected: " + path, Toast.LENGTH_LONG).show();

                                new ProccessFile().execute(path);

                            } catch (Exception e) {
                                Log.e("FileSelectorTestActivity", "File select error", e);
                            }
                        }
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private static class ProccessFile extends AsyncTask<String,Void,byte[]>
        {
            @Override
            protected byte[] doInBackground(String... params) {

                String uri = params[0];

                if(uri == null || uri == "")
                {
                    return null;
                }

                FileInputStream fileInputStream;

                File file = new File(uri);

                byte[] bFile = new byte[(int) file.length()];

                try {
                    //convert file into array of bytes
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bFile);
                    fileInputStream.close();

                    for (int i = 0; i < bFile.length; i++) {
                        System.out.print((char)bFile[i]);
                    }



                    return bFile;


                }catch(Exception e){
                    e.printStackTrace();

                }


                return null;
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);

                if(bytes == null)
                {
                    return;
                }

                final ParseFile file= new ParseFile(bytes);

                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e == null)
                        {
                            Log.i(TAG,"uploaded file complete");

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("pool");

// Retrieve the object by id
                            query.getInBackground("WMi3fNDwsm", new GetCallback<ParseObject>() {
                                public void done(ParseObject playlist, ParseException e) {
                                    if (e == null) {
                                        // Now let's update it with some new data. In this case, only cheatMode and score
                                        // will get sent to the Parse Cloud. playerName hasn't changed.
                                       playlist.add("playlistUris",file.getUrl());

                                      playlist.saveInBackground();
                                    }
                                }
                            });

                        }
                    }
                });
            }
        }
    }





}
