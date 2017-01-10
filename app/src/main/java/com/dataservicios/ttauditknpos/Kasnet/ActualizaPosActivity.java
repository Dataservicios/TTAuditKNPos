package com.dataservicios.ttauditknpos.Kasnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.dataservicios.ttauditknpos.AndroidCustomGalleryActivity;
import com.dataservicios.ttauditknpos.MainActivity;
import com.dataservicios.ttauditknpos.Model.Audit;
import com.dataservicios.ttauditknpos.Model.PollDetail;
import com.dataservicios.ttauditknpos.R;
import com.dataservicios.ttauditknpos.SQLite.DatabaseHelper;
import com.dataservicios.ttauditknpos.util.AuditUtil;
import com.dataservicios.ttauditknpos.util.GPSTracker;
import com.dataservicios.ttauditknpos.util.GlobalConstant;
import com.dataservicios.ttauditknpos.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ActualizaPosActivity extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = ActualizaPosActivity.class.getSimpleName();
    private SessionManager session;

    private Switch swSiNo ;
    private Button  bt_guardar;
    private EditText etComent;


    private String  fechaRuta, comentario="", type, region;

    private Integer user_id, company_id,store_id,road_id,audit_id,  poll_id;

    private LinearLayout lyOpciones;

    private int is_sino=0 ;
    private DatabaseHelper db;
    private ProgressDialog pDialog;
    private RadioGroup rgOpt;
    private String opt="";

    private RadioButton[] radioButton1Array;
    private PollDetail pollDetail;
    private Audit mAudit;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualiza_pos);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Pos");

        swSiNo = (Switch) findViewById(R.id.swSiNo);

        rgOpt=(RadioGroup) findViewById(R.id.rgOpt);
        radioButton1Array = new RadioButton[] {
                (RadioButton) findViewById(R.id.rbA),
                (RadioButton) findViewById(R.id.rbB),
                (RadioButton) findViewById(R.id.rbC),
        };

        gpsTracker = new GPSTracker(MyActivity);

        bt_guardar = (Button) findViewById(R.id.btGuardar);

        lyOpciones = (LinearLayout) findViewById(R.id.lyOpciones);
        etComent = (EditText) findViewById(R.id.etComent);

        Bundle bundle = getIntent().getExtras();
        company_id = GlobalConstant.company_id;
        store_id = bundle.getInt("store_id");
        road_id = bundle.getInt("road_id");
        audit_id = bundle.getInt("audit_id");

        poll_id = GlobalConstant.poll_id[1];

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;




        rgOpt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButton1Array[1].isChecked())
                {
                    etComent.setEnabled(true);
                    etComent.setVisibility(View.VISIBLE);
                    etComent.setText("");
                }
                else
                {
                    etComent.setEnabled(false);
                    etComent.setVisibility(View.INVISIBLE);
                    etComent.setText("");
                }
            }
        });



        swSiNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    is_sino = 1;
                    //clearRadioButtonCheck(radioButton2Array, false);
                    rgOpt.clearCheck();
                    lyOpciones.setVisibility(View.INVISIBLE);


                } else {

                    is_sino = 0;
                    //clearRadioButtonCheck(radioButton2Array, false);
                    lyOpciones.setVisibility(View.VISIBLE);
                    rgOpt.clearCheck();
                }
            }
        });



        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!swSiNo.isChecked()){

                    long id2 = rgOpt.getCheckedRadioButtonId();
                    if (id2 == -1){
                        Toast.makeText(MyActivity,"Seleccione una opción" , Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        for (int x = 0; x < radioButton1Array.length; x++) {
                            if(id2 ==  radioButton1Array[x].getId())  opt = poll_id.toString() + radioButton1Array[x].getTag();
                        }

                    }
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle("Guardar Encuesta");
                builder.setMessage("Está seguro de guardar todas las encuestas: ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        comentario = String.valueOf(etComent.getText()) ;

                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(1);
                        pollDetail.setOptions(1);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(0);
                        pollDetail.setComment(0);
                        pollDetail.setResult(is_sino);
                        pollDetail.setLimite(0);
                        pollDetail.setComentario("");
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCommentOptions(1);
                        pollDetail.setSelectdOptions(opt);
                        pollDetail.setSelectedOtionsComment(comentario);
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

            if(is_sino == 1) {
                if(!AuditUtil.insertPollDetail(pollDetail)) return false;
            } else{

                String time_close = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
                mAudit = new Audit();
                mAudit.setCompany_id(GlobalConstant.company_id);
                mAudit.setStore_id(store_id);
                mAudit.setId(audit_id);
                mAudit.setRoute_id(road_id);
                mAudit.setUser_id(user_id);
                mAudit.setLatitude_close(String.valueOf(gpsTracker.getLatitude()));
                mAudit.setLongitude_close(String.valueOf(gpsTracker.getLongitude()));
                mAudit.setLatitude_open(String.valueOf(GlobalConstant.latitude_open));
                mAudit.setLongitude_open(String.valueOf(GlobalConstant.longitude_open));
                mAudit.setTime_open(GlobalConstant.inicio);
                mAudit.setTime_close(time_close);

                if(!AuditUtil.insertPollDetail(pollDetail)) return false;
                if(!AuditUtil.closeAuditRoadStore(mAudit)) return false;
                if(!AuditUtil.closeAuditRoadAll(mAudit)) return false;
            }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();
                if(is_sino==1) {
                    Bundle argRuta = new Bundle();
                    argRuta.clear();
                    argRuta.putInt("store_id", store_id);
                    argRuta.putInt("audit_id", audit_id);
                    argRuta.putInt("road_id", road_id);


                    Intent intent;
                    intent = new Intent(MyActivity, FotoVoucherInicioActivity.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                    finish();


                } else if(is_sino==0){

                    finish();
                }



            } else {
                Toast.makeText(MyActivity , "No se pudo guardar la información intentelo nuevamente", Toast.LENGTH_LONG).show();
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






    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
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
