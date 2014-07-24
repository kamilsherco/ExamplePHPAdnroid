package com.example.examplephp;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
public class NewProductActivity extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputPrice;
    EditText inputDesc;
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";
    private String name ;
    private  String price;
    private String description;
 
    // url to create new product
    private static String url_create_product = "http://smacznesery.com/android_content/create_product.beta";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
 
        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputPrice = (EditText) findViewById(R.id.inputPrice);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
 
        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);
 
        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewProduct().execute();
            }
        });
    }
 
    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
             name = inputName.getText().toString();
             price = inputPrice.getText().toString();
             description = inputDesc.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
       
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_PRICE, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));
            Log.d("Params",name);
            Log.d("Params",price);
            Log.d("Params",description);
            
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,"POST", params);
           
            
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
               int success = json.getInt(TAG_SUCCESS);
            	
                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    startActivity(i);
 
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Create Response", "!!!!!!!!!!!!!!!!!!!!!!");
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }
}
