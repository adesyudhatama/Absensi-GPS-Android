package nad.ayt.absensigps;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parsing extends AsyncTask<Void,Integer,Integer> {
    Context c;
    ListView lv;
    String data;
    ArrayList<String> players=new ArrayList<>();
    ProgressDialog pd;
    public String statuz;

    public Parsing(Context c, String data, ListView lv) {
        this.c = c;
        this.data = data;
        this.lv = lv;
    }

    public Parsing() {

    }

    public String getStatusz (){
        return statuz;
    }
    public void setStatusz (String statuz){
        this.statuz = statuz;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Parsing");
        pd.setMessage("Parsing ....Please wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parse();
    }
    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer == 1)
        {
            //ADAPTER
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
            //ADAPT TO LISTVIEW
            lv.setAdapter(adapter);
            //LISTENET
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Snackbar.make(view,players.get(position),Snackbar.LENGTH_SHORT).show();;
                }
            });
        }else
        {
            //ADAPTER
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
            //ADAPT TO LISTVIEW
            lv.setAdapter(adapter);
            //LISTENET
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Snackbar.make(view,players.get(position),Snackbar.LENGTH_SHORT).show();;
                }
            });
            Toast.makeText(c,"Data not found",Toast.LENGTH_SHORT).show();
            players.add("\tTidak ada jadwal kuliah hari ini  ");
        }
        pd.dismiss();
    }
    //PARSE RECEIVED DATA

    private int parse()
    {
        String status ="";
        String lokasi ="";

        try
        {
            //ADD THAT DATA TO JSON ARRAY FIRST
            JSONArray ja=new JSONArray(data);
            //CREATE JO OBJ TO HOLD A SINGLE ITEM
            JSONObject jo=null;
            players.clear();
            //LOOP THRU ARRAY
            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                //RETRIOEVE NAME
                String name     =jo.getString("dosen");
                String matkul   =jo.getString("matkul");
                String nips     =jo.getString("nip");
                String prodi    =jo.getString("lokasi");
                String hari     =jo.getString("hari");
                String tanggal  =jo.getString("tanggal");
                String jam      =jo.getString("jam");
                String sts      =jo.getString("status");
                setStatusz(sts);
                String ruangan  =jo.getString("ruangan");
                if(prodi.equals("bil") || prodi.equals("Bil")) {
                    lokasi ="Palembang";
                }else{
                    lokasi ="Indralaya";
                }
                if(sts.equals("1")){
                    status ="TELAH HADIR";
                }else{
                    status ="BELUM HADIR";
                }

                //ADD IT TO OUR ARRAYLIST
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                String inputDateStr=tanggal;

                players.add(String.format("%-25s : %s", "Nama", name));
                players.add(String.format("%-28s : %s", "NIP", nips));
                players.add(String.format("%-21s : %s", "Mata Kuliah", matkul));
                try{
                    Date date = inputFormat.parse(inputDateStr);
                    String outputDateStr = outputFormat.format(date);
                    players.add(String.format("%-24s : %s, %s", "Tanggal", hari,outputDateStr));
                }catch(ParseException e){
                    e.printStackTrace();
                }
                players.add(String.format("%-25s : %s WIB", "Waktu", jam));
                players.add(String.format("%-24s : %s", "Kampus", lokasi));
                players.add(String.format("%-23s : %s", "Ruangan", ruangan));
                players.add(String.format("%-25s : %s", "Status ", status));

            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
