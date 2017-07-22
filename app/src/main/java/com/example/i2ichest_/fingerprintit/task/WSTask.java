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

public class WSTask extends AsyncTask<String,String,String> {

    public interface WSTaskListener{
        void onComplete(String response);
        void onError(String err);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private WSTaskListener listener;
    private Context context;

    public WSTask( Context context,WSTaskListener listener) {
        super();
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        Request.Builder builder = new Request.Builder();
<<<<<<< HEAD
=======
<<<<<<< HEAD
        //Request request = builder.url("http://192.168.1.33:8080".concat(params[0])).build();
        Request request = builder.url("http://10.0.0.98:8080".concat(params[0])).build();

=======
<<<<<<< HEAD
>>>>>>> 7991026667c00e630b537920f4956e93a42d3fc7
        Request request = builder.url("http://192.168.1.28:8080".concat(params[0])).build();;

<<<<<<< HEAD
=======
        //Request request = builder.url("http://192.168.1.33:8080".concat(params[0])).build();
        Request request = builder.url("http://10.0.0.98:8080".concat(params[0])).build();


=======
        Request request = builder.url("http://192.168.1.16:8080".concat(params[0])).build();;
>>>>>>> 8ac12f6689ad5f445f6ab5065704a8ddb4cb2a46
>>>>>>> 379fd2977193a628a263956ead35d04078604472
>>>>>>> 25476b2c52e5cce582165afa17bc2cc4266903fb
>>>>>>> 7991026667c00e630b537920f4956e93a42d3fc7
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "Not Success - code : " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error - " + e.getMessage();
        }
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

       /* ResponseModel model = gson.fromJson(s, ResponseModel.class);

        if(!model.getResult().equals("Welcome")){
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
