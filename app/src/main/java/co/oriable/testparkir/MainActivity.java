package co.oriable.testparkir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText email,pass,no_ken,name;
    Spinner jns_ken;
    TextView tv;

    RequestQueue requestQueue;
    String cek_login = "http://192.168.137.1:3000/api/user";
    String get_kendaraan = "http://192.168.137.1:3000/api/kendaraan/";
    String get_status = "http://192.168.137.1:3000/api/status/";
    String post_kendaraan = "http://192.168.137.1:3000/api/kendaraan";
    String get_statuscari = "http://192.168.137.1:3000/api/status/cari/?noken=";
    String put_status = "http://192.168.137.1:3000/api/status/";
    String del_kendaraan = "http://192.168.137.1:3000/api/kendaraan/?noken=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        tv = findViewById(R.id.textView);

        no_ken = findViewById(R.id.noken);
        name = findViewById(R.id.nama);
        jns_ken = findViewById(R.id.jenis);

        requestQueue = Volley.newRequestQueue(this);
    }

    // CEK LOGIN

    public void CekLogin(View view){
        final String mail = email.getText().toString();
        final String pas = pass.getText().toString();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, cek_login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("Volley",jsonObj.toString());
                            String codes = jsonObj.getString("code");
                            Log.e("Code",codes);
                            String status = jsonObj.getString("status");
                            Log.e("Status",status);
                            if(codes.equals("200")){
                                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                            }else if(codes.equals("203")){
                                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                            }else if (codes.equals("204")){
                                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error while calling REST API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", mail);
                params.put("pass", pas);
                return params;
            }

        };

        requestQueue.add(jsonObjReq);
    }




    //GET KENDARAAN

    public void getkendaraan(View view) {
        tv.setText("");
        JsonArrayRequest JsonArrayReq = new JsonArrayRequest(Request.Method.GET, get_kendaraan,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++) {
                                JSONObject kendaraan = response.getJSONObject(i);
                                String noken = kendaraan.getString("no_kendaraan");
                                String nama = kendaraan.getString("nama_pemilik");
                                String jenis = kendaraan.getString("jenis_kendaraan");

                                tv.append("No kendaraan: " +noken +"\nNama: " + nama +"\nJenis: " + jenis);
                                tv.append("\n\n");
                            }
                        } catch (JSONException e) {
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error while calling REST API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());
                    }
                });
        requestQueue.add(JsonArrayReq);
    }

    // GET STATUS

    public void getStatus(View view) {
        tv.setText("");
        JsonArrayRequest JsonArrayReq = new JsonArrayRequest(Request.Method.GET, get_status,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++) {
                                JSONObject kendaraan = response.getJSONObject(i);
                                String noken = kendaraan.getString("no_kendaraan");
                                Log.e("Noken", noken);
                                String status = kendaraan.getString("status");
                                Log.e("Status", status);
                                if(status.equals("1")) {
                                    tv.append("No kendaraan: " + noken + "\nStatus: Ya");
                                    tv.append("\n\n");
                                }else {
                                    tv.append("No kendaraan: " + noken + "\nStatus: Tidak");
                                    tv.append("\n\n");
                                }
                            }
                        } catch (JSONException e) {
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error while calling REST API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());
                    }
                });
        requestQueue.add(JsonArrayReq);
    }

    // GET STATUS SPESIFIK

    public void getStatusCari(View view) {
        final String noken = no_ken.getText().toString();
        tv.setText("");
        JsonArrayRequest JsonArrayReq = new JsonArrayRequest(Request.Method.GET, get_statuscari+noken,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++) {
                                JSONObject kendaraan = response.getJSONObject(i);
                                String noken = kendaraan.getString("no_kendaraan");
                                Log.e("Noken", noken);
                                String status = kendaraan.getString("status");
                                Log.e("Status", status);
                                if(status.equals("1")) {
                                    tv.append("No kendaraan: " + noken + "\nStatus: Ya");
                                    tv.append("\n\n");
                                }else {
                                    tv.append("No kendaraan: " + noken + "\nStatus: Tidak");
                                    tv.append("\n\n");
                                }
                            }
                        } catch (JSONException e) {
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error while calling REST API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());
                    }
                });
        requestQueue.add(JsonArrayReq);
    }

    // POST KENDARAAN

    public void Postkendaraan(View view){
        final String noken = no_ken.getText().toString();
        final String nama = name.getText().toString();
        final String jenis = jns_ken.getSelectedItem().toString();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, post_kendaraan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("Volley",jsonObj.toString());
                            String codes = jsonObj.getString("code");
                            Log.e("Code",codes);
                            //error duplicat
                            JSONObject error = jsonObj.getJSONObject("failed");
                            String msg = error.getString("sqlMessage");
                            Log.e("Error",msg);
                            if(codes.equals("200")){
                                JSONObject status = jsonObj.getJSONObject("status");
                                Toast.makeText(MainActivity.this, "Sukses Input", Toast.LENGTH_SHORT).show();
                            }else if(codes.equals("400")){
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error while calling REST API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String, String> params = new HashMap<>();
                params.put("noken", noken);
                params.put("nama", nama);
                params.put("jenis", jenis);
                return params;
            }

        };

        requestQueue.add(jsonObjReq);
    }

    // PUT STATUS

    public void PutStatus(View view){
        final String noken = no_ken.getText().toString();
        final String status = name.getText().toString();
        StringRequest putRequest = new StringRequest(Request.Method.PUT, put_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("Volley",jsonObj.toString());
                            String codes = jsonObj.getString("code");
                            Log.e("Code",codes);
                            if(codes.equals("200")){
                                Toast.makeText(MainActivity.this, "Sukses Merubah", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error while calling REST API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String, String> params = new HashMap<>();
                params.put("noken", noken);
                params.put("status", status);
                return params;
            }

        };

        requestQueue.add(putRequest);
    }

    // DELETE KENDARAAN

    public void DeleteKendaraan(View view){
        final String noken = no_ken.getText().toString();
        StringRequest DeleteRequest = new StringRequest(Request.Method.DELETE, del_kendaraan+noken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("Volley",jsonObj.toString());
                            String codes = jsonObj.getString("code");
                            Log.e("Code",codes);
                            if(codes.equals("200")){
                                Toast.makeText(MainActivity.this, "Sukses Delete", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error while calling REST API", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());
                    }
                });

        requestQueue.add(DeleteRequest);
    }





}


