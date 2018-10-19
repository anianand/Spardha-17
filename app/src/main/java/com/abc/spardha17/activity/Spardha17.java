package com.abc.spardha17.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.abc.spardha17.R;
import com.abc.spardha17.app.AppController;
import com.abc.spardha17.fragments.Strings.strings;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Spardha17 extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    SharedPreferences sharedpreferences,result,fixture,contact,rules;

    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
         }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spardha17);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                startActivity(new Intent(Spardha17.this,Launch.class));
                getdatafromserver();

//                finish();
//            }

//        }, 2000L);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        sharedpreferences = this.getSharedPreferences("Data", Context.MODE_PRIVATE);
        fixture = this.getSharedPreferences("Fixture", Context.MODE_PRIVATE);
        contact = this.getSharedPreferences("Contact", Context.MODE_PRIVATE);
        result = this.getSharedPreferences("Result", Context.MODE_PRIVATE);
        rules = this.getSharedPreferences("Rules", Context.MODE_PRIVATE);
                startActivity(new Intent(Spardha17.this,Launch.class));
finish();
        // Set up the user interaction to manually show or hide the system UI.
    }

    private void getdatafromserver() {
        String tag_json_arry = "json_array_req";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        System.out.println("yoyo");
        final strings s=new strings();

        JsonArrayRequest req = new JsonArrayRequest("https://spardha-17.firebaseio.com/.json?shallow=true'",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        SharedPreferences.Editor editorrules = rules.edit();
                        SharedPreferences.Editor editorcontact = contact.edit();
                        SharedPreferences.Editor editorres = result.edit();
                        SharedPreferences.Editor editorfixtures = fixture.edit();

                        editor.putString("responses" , response.toString());
                        editor.commit();
                        Log.d("RESPONSEFIREBASE", response.toString());
                        System.out.println(response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jresponse = response.getJSONObject(i);
                                String game=jresponse.keys().next();
                                int position=0;
                                for (int j=0;j<s.titles.length ;j++){

                                    if(s.titles[j].equals(game)){
                                        position=j;
                                        break;
                                    }
                                }
                            JSONObject jresponsegame=jresponse.getJSONObject(game);
                            Log.d("RESPONSE",jresponse.toString());
                            Log.d("RESPONSEPOSITION",position+"");

                            editorrules.putString("responser"+position,jresponsegame.getString("Rules"));
                            editorcontact.putString("responsec"+position,jresponsegame.getString("Contact"));
                            editorres.putString("responses"+position,jresponsegame.getString("Results"));

                            editorfixtures.putString("response"+position,jresponsegame.getString("Fixtures"));
                            Log.d("RESPONSE","responser"+position+"\n"+jresponsegame.getString("Rules"));
                            editorrules.commit();
                            editorcontact.commit();
                            editorres.commit();
                            editorfixtures.commit();


                            }

                        } catch (JSONException e) {
                            Log.d("RESPONSEERROE","e"+e);

                            e.printStackTrace();
                        }
                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSEFIREBASE", "ERROR"+ error);

                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);

    }




}
