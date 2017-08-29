package com.example.i2ichest_.fingerprintit.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WSTaskPost extends AsyncTask<String,String,String> {

    public interface WSTaskListener{
        void onComplete(String response);
        void onError(String err);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();;
    private WSTaskListener listener;
    private Context context;

    public WSTaskPost(Context context, WSTaskListener listener) {
        super();
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        RequestBody body = RequestBody.create(JSON, params[1]);

        Request request = new Request.Builder().url("http://10.0.0.78:8080".concat(params[0])).post(body).build();

        try{
            Response response = client.newCall(request).execute();
            return response.body().string();

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(String s){
        super.onPostExecute(s);
        if(listener == null){
            return;
        }
        if(s==null){
            listener.onError("Error ,please try again");
        }
        Gson gson = new GsonBuilder().create();
       // Log.d("RESPONSE ",s);
       // ResponseModel model = gson.fromJson(s, ResponseModel.class);
        //Log.d(model.getResult()," RESULT!");
    /*    if(!model.getResult().equals("Welcome")){
            if(model.getResult() ==null) {
                listener.onError(context.getString(R.string.global));
                return;
            }
                listener.onError(model.getResult().toString());
                return;
        }*/
        listener.onComplete(s);
    }

}
