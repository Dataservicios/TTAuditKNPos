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
import android.widget.TextView;
import android.widget.Toast;


import com.dataservicios.ttauditknpos.Model.PollDetail;
import com.dataservicios.ttauditknpos.R;
import com.dataservicios.ttauditknpos.SQLite.DatabaseHelper;
import com.dataservicios.ttauditknpos.util.AuditUtil;
import com.dataservicios.ttauditknpos.util.GlobalConstant;
import com.dataservicios.ttauditknpos.util.SessionManager;

import java.util.HashMap;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Jaime on 4/11/2016.
 */

public class PalmeraBloqueador extends Activity {

    private Activity MyActivity = this ;
    private static final String LOG_TAG = PalmeraBloqueador.class.getSimpleName();
    private SessionManager session;

    private Switch swSiNo ;
    private Button btSave;
    private EditText etComentSi,etComentNo;
    private TextView tv_Pregunta;
    private LinearLayout lyOpcionesNo,lyOpcionesSi;

    private String tipo,cadenaruc, fechaRuta, comentario="", type, region;

    private Integer user_id,store_id, poll_id, company_id , store_id_palmera;

    int  is_sino=1 ;


    private DatabaseHelper db;

    private ProgressDialog pDialog;


    private RadioGroup rgOptSi, rgOptNo;
    private String opt="";

    private RadioButton[] radioButtonSiArray;
    private RadioButton[] radioButtonNoArray;


    PollDetail mPollDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.palmera_bloqueador);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        swSiNo = (Switch) findViewById(R.id.swSiNo);

        lyOpcionesNo = (LinearLayout) findViewById(R.id.lyOpcionesNo);
        lyOpcionesSi = (LinearLayout) findViewById(R.id.lyOpcionesSi);

        rgOptSi=(RadioGroup) findViewById(R.id.rgOptSi);
        rgOptNo=(RadioGroup) findViewById(R.id.rgOptNo);

        radioButtonSiArray = new RadioButton[] {
                (RadioButton) findViewById(R.id.rbASi),
                (RadioButton) findViewById(R.id.rbBSi),
                (RadioButton) findViewById(R.id.rbCSi),
                (RadioButton) findViewById(R.id.rbDSi),


        };

        radioButtonNoArray = new RadioButton[] {
                (RadioButton) findViewById(R.id.rbANo),
                (RadioButton) findViewById(R.id.rbBNo),
                (RadioButton) findViewById(R.id.rbCNo),
                (RadioButton) findViewById(R.id.rbDNo),

        };

        // tv_Pregunta = (TextView) findViewById(R.id.tvPregunta);
        btSave = (Button) findViewById(R.id.btSave);

        //et_Comentario = (EditText) findViewById(R.id.etComentario);
        etComentSi = (EditText) findViewById(R.id.etComentSi);
        etComentNo = (EditText) findViewById(R.id.etComentNo);

        Bundle bundle = getIntent().getExtras();
        company_id = GlobalConstant.company_id;
        store_id = bundle.getInt("store_id");
        poll_id = GlobalConstant.poll_id_palmera[0];


        db = new DatabaseHelper(getApplicationContext());



        //poll_id = 72 , solo para exhibiciones de bayer, directo de la base de datos

        pDialog = new ProgressDialog(MyActivity);
        pDialog.setMessage("Cargando...");
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        rgOptSi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButtonSiArray[3].isChecked())
                {
                    etComentSi.setEnabled(true);
                    etComentSi.setVisibility(VISIBLE);
                    etComentSi.setText("");

                }
                else
                {
                    etComentSi.setEnabled(false);
                    etComentSi.setVisibility(View.INVISIBLE);
                    etComentSi.setText("");

                }
            }
        });

        rgOptNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioButtonNoArray[3].isChecked())
                {
                    etComentNo.setEnabled(true);
                    etComentNo.setVisibility(VISIBLE);
                    etComentNo.setText("");

                }
                else
                {
                    etComentNo.setEnabled(false);
                    etComentNo.setVisibility(View.INVISIBLE);
                    etComentNo.setText("");

                }
            }
        });




        swSiNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_sino = 1;
                    lyOpcionesSi.setEnabled(true);
                    lyOpcionesSi.setVisibility(VISIBLE);
                    rgOptSi.clearCheck();

                    lyOpcionesNo.setEnabled(false);
                    lyOpcionesNo.setVisibility(INVISIBLE);

                } else {
                    is_sino = 0;
                    lyOpcionesNo.setEnabled(true);
                    lyOpcionesNo.setVisibility(VISIBLE);
                    rgOptNo.clearCheck();

                    lyOpcionesSi.setEnabled(false);
                    lyOpcionesSi.setVisibility(INVISIBLE);

                }
            }
        });




        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(swSiNo.isChecked()){

                    long id1 = rgOptSi.getCheckedRadioButtonId();
                    if (id1 == -1){
                        Toast.makeText(MyActivity,"Selecione una opción" , Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        for (int x = 0; x < radioButtonSiArray.length; x++) {
                            if(id1 ==  radioButtonSiArray[x].getId())  opt = poll_id.toString() + radioButtonSiArray[x].getTag();
                        }

                    }

                    comentario = String.valueOf(etComentSi.getText()) ;

                } else {
                    long id1 = rgOptNo.getCheckedRadioButtonId();
                    if (id1 == -1){
                        Toast.makeText(MyActivity,"Selecione una opción" , Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        for (int x = 0; x < radioButtonNoArray.length; x++) {
                            if(id1 ==  radioButtonNoArray[x].getId())  opt = poll_id.toString() + radioButtonNoArray[x].getTag();
                        }

                    }
                    comentario = String.valueOf(etComentNo.getText()) ;
                }

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
                        mPollDetail.setComment(0);
                        mPollDetail.setResult(is_sino);
                        mPollDetail.setLimite(0);
                        mPollDetail.setComentario("");
                        mPollDetail.setAuditor(user_id);
                        mPollDetail.setProduct_id(0);
                        mPollDetail.setCategory_product_id(0);
                        mPollDetail.setPublicity_id(0);
                        mPollDetail.setCompany_id(GlobalConstant.company_id_palmera);
                        mPollDetail.setCommentOptions(1);
                        mPollDetail.setSelectdOptions(opt);
                        mPollDetail.setSelectedOtionsComment(comentario);
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


            store_id_palmera = AuditUtil.duplicateStore(GlobalConstant.company_id_palmera,store_id);
            if(store_id_palmera == 0) {
                return false;
            } else {
                mPollDetail.setStore_id(store_id_palmera);
            }

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

                if(is_sino==1){
                    Bundle argRuta = new Bundle();
                    argRuta.clear();
                    argRuta.putInt("store_id", store_id_palmera);

                    Intent intent;
                    intent = new Intent(MyActivity, PalmeraPrecio.class);
                    intent.putExtras(argRuta);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }


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
