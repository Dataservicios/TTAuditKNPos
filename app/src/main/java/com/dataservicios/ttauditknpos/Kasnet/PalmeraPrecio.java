package com.dataservicios.ttauditknpos.Kasnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.dataservicios.ttauditknpos.Model.PollDetail;
import com.dataservicios.ttauditknpos.R;
import com.dataservicios.ttauditknpos.SQLite.DatabaseHelper;
import com.dataservicios.ttauditknpos.util.AuditUtil;
import com.dataservicios.ttauditknpos.util.GlobalConstant;
import com.dataservicios.ttauditknpos.util.SessionManager;

import java.util.HashMap;


/**
 * Created by Jaime on 4/11/2016.
 */

public class PalmeraPrecio extends Activity {
    private Activity MyActivity = this ;
    private static final String LOG_TAG = PalmeraPrecio.class.getSimpleName();
    private SessionManager session;
    private Button btSave;
    private EditText etComent;

    private String tipo,cadenaruc, fechaRuta, comentario="", type, region;
    private Integer user_id,store_id, poll_id, company_id ;
    private DatabaseHelper db;

    private ProgressDialog pDialog;



    PollDetail mPollDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.palmera_precio);
        getActionBar().setDisplayHomeAsUpEnabled(true);



        // tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);
        btSave = (Button) findViewById(R.id.btSave);

        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        etComent = (EditText) findViewById(R.id.etComent);


        Bundle bundle = getIntent().getExtras();
        company_id = GlobalConstant.company_id;
        store_id = bundle.getInt("store_id");

        poll_id = GlobalConstant.poll_id_palmera[1];


        db = new DatabaseHelper(getApplicationContext());



        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;




        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                comentario = String.valueOf(etComent.getText().toString()) ;


                AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
                builder.setTitle(R.string.save);
                builder.setMessage(R.string.saveInformation);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPollDetail = new PollDetail();
                        mPollDetail.setPoll_id(poll_id);
                        mPollDetail.setStore_id(store_id);
                        mPollDetail.setSino(1);
                        mPollDetail.setOptions(1);
                        mPollDetail.setLimits(0);
                        mPollDetail.setMedia(0);
                        mPollDetail.setComment(1);
                        mPollDetail.setResult(0);
                        mPollDetail.setLimite(0);
                        mPollDetail.setComentario(comentario.toString());
                        mPollDetail.setAuditor(user_id);
                        mPollDetail.setProduct_id(0);
                        mPollDetail.setCategory_product_id(0);
                        mPollDetail.setPublicity_id(0);
                        mPollDetail.setCompany_id(GlobalConstant.company_id_palmera);
                        mPollDetail.setCommentOptions(1);
                        mPollDetail.setSelectdOptions("");
                        mPollDetail.setSelectedOtionsComment("");
                        mPollDetail.setPriority("0");
                        new loadPoll().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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



    class loadPoll extends AsyncTask<Void, Integer , Boolean> {
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


            if(!AuditUtil.insertPollDetail(mPollDetail)) return false;


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                // loadLoginActivity();


                    finish();


            } else {
                Toast.makeText(MyActivity , R.string.saveError, Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

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
        Toast.makeText(MyActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

}

