package com.example.shradha.volly;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
AlertDialog.Builder builder;
    Button button, choose, button3,upload;
    TextView textView;
    EditText Name;
    ImageView imageView;
    String name;
    String url = "http://192.168.43.206:8080/volly.php";
    String url1 = "http://192.168.43.206:8080/cart.png";
    RequestQueue queue;
    private final int IMG_REQUEST=1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        choose = findViewById(R.id.choose);
        button3 = findViewById(R.id.button3);
        upload = findViewById(R.id.upload);
        textView = findViewById(R.id.tv);
        Name = findViewById(R.id.editText);
        builder= new AlertDialog.Builder(MainActivity.this);
        Cache cahe = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username= Name.getText().toString();


                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.206:8080/sample/data.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(MainActivity.this, "dfdsfsd"+response, Toast.LENGTH_SHORT).show();
                        Log.i("My success",""+response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, "my error :"+error, Toast.LENGTH_LONG).show();
                        Log.i("My error",""+error);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> map = new HashMap<String, String>();
                        map.put("name",username);


                        return map;
                    }
                };
                queue.add(request);

            }
        });



    }

    public void click(View v) {

        queue = Volley.newRequestQueue(this);
        Cache cahe = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cahe, network);
        queue.start();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText(response);
                        queue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
                queue.stop();
            }
        });
        queue.add(stringRequest);
    }

    public void click2(View v) {

        queue = Volley.newRequestQueue(this);
        Cache cahe = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cahe, network);
        queue.start();
        ImageRequest stringRequest = new ImageRequest(url1, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//textView.setText("error"+error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }


    public void sendd(View v) {
        name = Name.getText().toString();
      //  Bitmap bm=imageView.getI
      StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://192.168.43.206:8080/sample/data.php", new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
builder.setTitle("server response");
builder.setMessage("Response"+response);
builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Name.setText("");
    }
});
AlertDialog alertDialog=builder.create();
alertDialog.show();
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
      }){
          protected Map<String,String> getParams() throws AuthFailureError {
              Map<String,String>  param=new HashMap<String, String>();
          //    Map<String,Bitmap>  param1=new HashMap<String, Bitmap>();
        //      param1.put("image",bm);
              param.put("name",name);
return super.getParams();
          }
      };
    }


   public void   choose(View v){
switch (v.getId()){
    case R.id.choose:
        selectimage();
    break;
    case R.id.upload:
       uploadimage();
        break;
}
   }


   public void selectimage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
   }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQUEST&& requestCode==RESULT_OK&&data!=null){
            Uri path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
              //  imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadimage(){
StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://192.168.43.206:8080/sample/data.php", new Response.Listener<String>() {
    @Override
    public void onResponse(String response) {
        try {
           JSONObject jsonObject=new JSONObject(response);
            String response1=jsonObject.getString("response");
            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {

    }
}){
    protected Map<String,String> getParams() throws AuthFailureError {
        Map<String, String> param = new HashMap<String, String>();
        param.put("name",Name.getText().toString().trim());
        param.put("image",bitmaptostring(bitmap));
        Log.d("image_string", "" + bitmaptostring(bitmap));
        return super.getParams();
    }

};



    }

private String bitmaptostring(Bitmap bitmap){
  ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
       byte[] imgbyte= byteArrayOutputStream.toByteArray();
       return Base64.encodeToString(imgbyte,Base64.DEFAULT);
}
}




