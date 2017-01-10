package com.dataservicios.ttauditknpos.Kasnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataservicios.ttauditknpos.AndroidCustomGalleryActivity;
import com.dataservicios.ttauditknpos.Model.Audit;
import com.dataservicios.ttauditknpos.Model.PollDetail;
import com.dataservicios.ttauditknpos.R;
import com.dataservicios.ttauditknpos.SQLite.DatabaseHelper;
import com.dataservicios.ttauditknpos.util.AuditUtil;
import com.dataservicios.ttauditknpos.util.GPSTracker;
import com.dataservicios.ttauditknpos.util.GlobalConstant;
import com.dataservicios.ttauditknpos.util.SessionManager;

import java.util.HashMap;

public class FotoStikerPosActivity extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = FotoStikerPosActivity.class.getSimpleName();
    private SessionManager session;

    private Button bt_photo, bt_guardar;
    private EditText etComent;


    private String    comentario="" ;

    private Integer user_id, company_id,store_id,road_id,audit_id,  poll_id;


    private DatabaseHelper db;
    private ProgressDialog pDialog;


    private PollDetail pollDetail;
    private Audit mAudit;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_stiker_pos);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Foto Stiker POS");

        gpsTracker = new GPSTracker(MyActivity);

        bt_guardar = (Button) findViewById(R.id.btGuardar);
        bt_photo = (Button) findViewById(R.id.btPhoto);
        //etComent = (EditText) findViewById(R.id.etComent);

        Bundle bundle = getIntent().getExtras();
        company_id = GlobalConstant.company_id;
        store_id = bundle.getInt("store_id");
        road_id = bundle.getInt("road_id");
        audit_id = bundle.getInt("audit_id");

        poll_id = GlobalConstant.poll_id[4];

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage(getString(R.string.text_loading));
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;


        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle(R.string.message_save);
                builder.setMessage(R.string.message_save_information);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       // comentario = String.valueOf(etComent.getText()) ;

                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(0);
                        pollDetail.setOptions(0);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(1);
                        pollDetail.setComment(0);
                        pollDetail.setResult(0);
                        pollDetail.setLimite(0);
                        pollDetail.setComentario("");
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCommentOptions(0);
                        pollDetail.setSelectdOptions("");
                        pollDetail.setSelectedOtionsComment("");
                        pollDetail.setPriority("0");

                        new loadPoll().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                builder.setCancelable(false);

            }
        });

    }

    private void takePhoto() {

        Intent i = new Intent( MyActivity, AndroidCustomGalleryActivity.class);
        Bundle bolsa = new Bundle();

        bolsa.putString("store_id", String.valueOf(store_id));
        bolsa.putString("product_id", String.valueOf("0"));
        bolsa.putString("publicities_id", String.valueOf("0"));
        bolsa.putString("poll_id", String.valueOf(poll_id));
        bolsa.putString("sod_ventana_id", String.valueOf("0"));
        bolsa.putString("company_id", String.valueOf(GlobalConstant.company_id));
        bolsa.putString("url_insert_image", GlobalConstant.dominio + "/insertImagesProductPollAlicorp");
        bolsa.putString("tipo", "1");
        i.putExtras(bolsa);
        startActivity(i);
    }

    class loadPoll extends AsyncTask<Void , Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            //tvCargando.setText("Cargando Product...");
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub


            if(!AuditUtil.insertPollDetail(pollDetail)) return false;


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();

                Bundle argRuta = new Bundle();
                argRuta.clear();
                argRuta.putInt("store_id", store_id);
                argRuta.putInt("audit_id", audit_id);
                argRuta.putInt("road_id", road_id);
                Intent intent;

                intent = new Intent(MyActivity, ModeloPosActivity.class);
                intent.putExtras(argRuta);
                startActivity(intent);
                finish();



            } else {
                Toast.makeText(MyActivity , R.string.message_no_save_data, Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                Intent a = new Intent(this,PanelAdmin.class);
//                //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }






    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(MyActivity,R.string.mesaage_on_back, Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
}
