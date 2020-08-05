package nad.ayt.absensigps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        String url="http://adesyudhatama.tk/android/getdata.php?nip="+email;
        final ListView lv= (ListView) findViewById(R.id.listview);
        final GetData d=new GetData(this,url,lv);
        Button button = (Button) findViewById(R.id.submit);
        View buttons = (View) findViewById(R.id.submit);
        Button refresh = (Button) findViewById(R.id.refresh);
        try {
            String webPage = "http://adesyudhatama.tk/android/getstatus.php?nip="+email;
            URL urlz = new URL(webPage);
            URLConnection urlConnection = urlz.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();
            //Log.d("STATUSS ",result);
            int anu = Integer.parseInt(result.trim());
            if(anu == 0){
                button.setEnabled(true);
            }else if(anu==1){
                button.setEnabled(false);
            }else if(anu == 10){
                button.setEnabled(false);
                buttons.setVisibility(View.GONE);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
                    URL u = new URL("http://adesyudhatama.tk/android/setstatus.php?nip="+email+"&status=1"+"&keterangan="+GetActivity.keterangan);
                    URLConnection urlConnection = u.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    int numCharsRead;
                    char[] charArray = new char[1024];
                    StringBuffer sb = new StringBuffer();
                    while ((numCharsRead = isr.read(charArray)) > 0) {
                        sb.append(charArray, 0, numCharsRead);
                    }
                    String result = sb.toString();
                    // Log.d("STATUS ",result);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Toast.makeText(ProfileActivity.this,"Absensi Sukses", Toast.LENGTH_LONG).show();
            }
        });
        d.execute();

    }

    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.putString(Config.EMAIL_SHARED_PREF, "");
                        editor.commit();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            logout();
        }else  if (id == R.id.refresh) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Toast.makeText(ProfileActivity.this,"Refreshed", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}