package com.github.droidcmusclient;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.mustard.jmus.CmusClient;
import com.github.mustard.jmus.command.Playback;
import com.github.mustard.jmus.command.Volume;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private boolean play = true;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//            View title = getWindow().findViewById(android.R.id.title);
//            View titleBar = (View) title.getParent();
//            titleBar.setBackgroundColor(Color.RED);

            ImageButton btnPrev = (ImageButton) rootView.findViewById(R.id.btnPrev);
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PLAYBACK", "Clicked previous button");
                    new AsyncTask<Integer, Integer, Integer>() {
                        @Override
                        protected Integer doInBackground(Integer... integers) {
                            Log.i("PLAYBACK", "Send Previous command");
                            CmusClient client = getClient();
                            try {
                                client.issueCommand(Playback.PREV);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return 1;
                        }
                    }.execute(1, 1);
                }
            });

            final ImageButton btnPlay = (ImageButton) rootView.findViewById(R.id.btnPlayOrPause  );
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PLAYBACK", "Clicked play / pause button");
                    if (play) {
                        Log.i("PLAYBACK", "Currently playing, sending Pause command");
                        new AsyncTask<Integer, Integer, Integer>() {
                            @Override
                            protected Integer doInBackground(Integer... integers) {
                                Log.i("TAG", "Toggle Playback");
                                CmusClient client = getClient();
                                try {
                                    client.issueCommand(Playback.PAUSE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return 1;
                            }
                        }.execute(1, 1);
                        btnPlay.setBackgroundResource(R.drawable.stop);
                        play = false;
                    } else {
                        Log.i("PLAYBACK", "Currently paused, sending Play command");
                        new AsyncTask<Integer, Integer, Integer>() {
                            @Override
                            protected Integer doInBackground(Integer... integers) {
                                Log.i("TAG", "Toggle Playback");
                                CmusClient client = getClient();
                                try {
                                    client.issueCommand(Playback.PLAY);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return 1;
                            }
                        }.execute(1, 1);
                        btnPlay.setBackgroundResource(R.drawable.play);
                        play = true;
                    }
                }
            });


            ImageButton btnNext = (ImageButton) rootView.findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PLAYBACK", "Clicked next button");
                    new AsyncTask<Integer, Integer, Integer>() {

                        @Override
                        protected Integer doInBackground(Integer... ints) {
                            Log.i("TAG", "Next Track");
                            CmusClient client = getClient(); // CmusClient("192.168.3.99", 9898, "foobar");
                            try {
                                client.issueCommand(Playback.NEXT);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return 1;
                        }

                        ;
                    }.execute(1, 1);
                }
            });

            ImageButton btnVolUp = (ImageButton) rootView.findViewById(R.id.btnVolUp);
            btnVolUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Integer, Integer, Integer>() {

                        @Override
                        protected Integer doInBackground(Integer... ints) {
                            Log.i("PLAYBACK", "Send Volume UP Command");
                            CmusClient client = getClient();
                            try {
                                client.issueCommand(Volume.UP);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return 1;
                        }

                        ;
                    }.execute(1, 1);
                }
            });

            ImageButton btnVolDown = (ImageButton) rootView.findViewById(R.id.btnVolDown);
            btnVolDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Integer, Integer, Integer>() {

                        @Override
                        protected Integer doInBackground(Integer... ints) {
                            Log.i("PLAYBACK", "Send Volume DOWN Command");
                            CmusClient client = getClient();
                            try {
                                client.issueCommand(Volume.DOWN);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return 1;
                        }

                        ;
                    }.execute(1, 1);
                }
            });

            return rootView;
        }

        private CmusClient getClient() {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            for (Map.Entry<String, ?> data : prefs.getAll().entrySet()) {
//                Log.i("PREFS", "Pref: " + data.getKey() + ":" + data.getValue());
//            }
            String host = prefs.getString("host_name", "");
            // Can't find a way to persist the preference as an Integer, from the GUI even though
            // The input can only use the numeric keypad
//            int port    = prefs.getInt   ("host_port", 9898);
            int port = Integer.parseInt(prefs.getString("host_port", "9898"));
            String pass = prefs.getString("host_password", "");
            Log.i("SETTINGS", "Host: " + host + " Port: " + port + " Password: " + pass);
            return new CmusClient(host, port, pass);
        }
    }
}
