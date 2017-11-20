package com.snapdragonbeast.rest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class RestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
    }

    public void chuckjokesclick(View view) {
        Ion.with(this).load("https://api.icndb.com/jokes/random").asString()
                .setCallback(new FutureCallback<String>() {

                    /*
                   on jsoon received
                   {
                   "type": "success",
                   "value": {
                   "id": 115,
                   "joke": "When Chuck Norris is in a crowded area, he doesn't walk around people. He walks through them.",
                   "categories": [] } }
                   */
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            JSONObject json = new JSONObject(result);
                            JSONObject value = json.getJSONObject("value");
                            String joke = value.getString("joke");
                            TextView textView = (TextView)findViewById(R.id.output);
                            textView.setText(joke);


                        } catch (JSONException json) {
                            Log.wtf("help", json);
                        }
                    }
                });
    }

    public void catpicsclick(View view) {

        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridCat);
gridLayout.removeAllViews();
        Ion.with(this).load("http://thecatapi.com/api/images/get?format=xml&results_per_page=6")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //xml to json parse data
                        try{

                            JSONObject json = XML.toJSONObject(result);
/*
{
  "response": {
    "data": {
      "images": {
        "image": {
          "url": "
http://25.media.tumblr.com/tumblr_lnxqxx2bXT1qbt33io1_1280.jpg
",
          "id": "5pq",
          "source_url": "http://thecatapi.com/?id=5pq"
        }
      }
    }
  }
}
 */

                            JSONArray array = json.getJSONObject("response")
                                    .getJSONObject("data")
                                    .getJSONObject("images")
                                    .getJSONArray("image");

                            for(int i=0 ; i <array.length();i++){
                                JSONObject image = array.getJSONObject(i);
                                String url = image.getString("url");
                                Log.wtf("url",url);
                                loadImmage(url);
                            }
                        }catch ( JSONException json){
                            Log.w("help",json);
                        }
                    }
                });
    }

    private void loadImmage(String url) {
        ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams  params= new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(params);
        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridCat);
        gridLayout.addView(imageView);

        Picasso.with(this)
                .load(url)
                .into(imageView);
    }
}
