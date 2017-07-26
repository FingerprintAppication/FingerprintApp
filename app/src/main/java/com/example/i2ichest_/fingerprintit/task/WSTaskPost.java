package com.example.i2ichest_.fingerprintit.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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
    private OkHttpClient client = new OkHttpClient();
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

<<<<<<< HEAD
        Request request = new Request.Builder().url("http://10.0.0.100:8080".concat(params[0])).post(body).build();
        //Request request = new Request.Builder().url("http://192.168.1.33:8080".concat(params[0])).post(body).build();

=======
        Request request = new Request.Builder().url("http://192.168.1.127:8080".concat(params[0])).post(body).build();


>>>>>>> 35b51a2ae4936ee3e7ab747bc281893c7b5a4fdb
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
