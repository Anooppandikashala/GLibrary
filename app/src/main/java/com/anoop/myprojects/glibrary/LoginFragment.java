package com.anoop.myprojects.glibrary;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText txtUsername, txtPassword;

    // login button
    Button btnLogin;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    //SessionManager session;

    String USERNAME;

    String JSON_STRING,json_string,code;
    JSONArray jsonArray;
    JSONObject jsonObject;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        txtUsername = (EditText) view.findViewById(R.id.et_registerNumber);
        txtPassword = (EditText) view.findViewById(R.id.et_password);

        Toast.makeText(getActivity(), "User Login Status: " , Toast.LENGTH_LONG).show();


        // Login button
        btnLogin = (Button) view.findViewById(R.id.btn_login);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){

            //
            //textView.setText("");

        }
        else {

            open();
        }


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString();
                USERNAME=username;
                String password = txtPassword.getText().toString();

                // Check if username, password is filled
                if(username.trim().length() > 0 && password.trim().length() > 0){

                    LoginFragment.BackgroundTask backgroundTask =new LoginFragment.BackgroundTask();
                    backgroundTask.execute(username,password);
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    //if(username.equals("test") && password.equals("test")){
                    /*if(code.trim().equals("login_success")){

                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data
                        session.createLoginSession("Android Hive", "anroidhive@gmail.com");

                        // Staring MainActivity
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();

                    }else{
                        // username / password doesn't match
                        alert.showAlertDialog(Login.this, "Login failed..", "Username/Password is incorrect", false);
                    }*/
                }else{
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(getActivity(), "Login failed..", "Please enter username and password", false);
                }

            }
        });

        return  view;
    }

    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Network Not Found ! \nPlease enable network and try again.");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        getActivity().finish();
                    }
                });

        /*alertDialogBuilder.setNegativeButton("",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    class BackgroundTask extends AsyncTask<String,Void,String>
    {


        String addInfoUrl;

        @Override
        protected void onPreExecute() {

            addInfoUrl ="https://groupprojectdesign.000webhostapp.com/loginApp.php";

        }

        @Override
        protected String doInBackground(String... args) {

            String usrname,password;
            usrname=args[0];
            password=args[1];

            System.out.println("User :"+usrname);
            System.out.println("Pass :"+password);

            try {
                URL url = new URL(addInfoUrl);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));


                String data_string = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(usrname,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream =httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));


                StringBuilder stringBuilder =new StringBuilder();

                JSON_STRING = bufferedReader.readLine();

                System.out.println(JSON_STRING);

                stringBuilder.append(JSON_STRING+"\n");


                /*while ((JSON_STRING = bufferedReader.readLine())!=null){

                    stringBuilder.append(JSON_STRING+"\n");



                }*/




                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            //textView.setText(result);
            json_string =result;

            System.out.println("json string :"+json_string);

            try {
                jsonObject=new JSONObject(json_string);

                jsonArray=jsonObject.getJSONArray("server_response");


                JSONObject JO= jsonArray.getJSONObject(0);

                code=JO.getString("code");

                if(code.trim().equals("login_success")){

                    // Creating user login session
                    // For testing i am stroing name, email as follow
                    // Use user real data
                    //session.createLoginSession("Android Hive", "anroidhive@gmail.com");

                    // Staring MainActivity
                    Intent i = new Intent(getActivity(), Main2Activity.class);
                    i.putExtra("name",USERNAME);
                    i.putExtra("email","anroidhive@gmail.com");
                    startActivity(i);
                    getActivity().finish();

                }else{
                    // username / password doesn't match
                    alert.showAlertDialog(getActivity(), "Login failed..", "Username/Password is incorrect", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
