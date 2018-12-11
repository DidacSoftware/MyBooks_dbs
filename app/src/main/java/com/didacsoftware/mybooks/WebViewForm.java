package com.didacsoftware.mybooks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewForm extends AppCompatActivity {

    WebView wbvForm;
    JavaScriptInterface JSInterface;

    TextView txvDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_form);


        txvDatos = findViewById(R.id.wvf_txvDatos);

        wbvForm = findViewById(R.id.wbv_wbvForm);


        wbvForm.getSettings().setJavaScriptEnabled(true);
        // register class containing methods to be exposed to JavaScript

        JSInterface = new JavaScriptInterface(this);
        wbvForm.addJavascriptInterface(JSInterface, "JSInterface");

        wbvForm.loadUrl("file:///android_asset/form.html");

    }


    public class JavaScriptInterface {

      Context mContext;

        /** Instantiate the interface and set the context*/
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @android.webkit.JavascriptInterface
        public void changeActivity(String sNombre, String sNumero, String sFecha)
        {

            if (sNombre.length()<=0 || sNombre.length()<=0||sFecha.length()<=0){

                if (sNombre.length()<=0){
                    sNombre="error Nombre";
                }
                if (sNumero.length()<=0){
                    sNumero="error Numero";
                }
                if (sFecha.length()<=0){
                    sFecha="error Fecha";
                }





                txvDatos.setText("Nombre: "+sNombre+
                        "\nNúmero tarjeta: "+sNumero+
                        "\nFecha: "+sFecha);
                txvDatos.setTextColor(Color.parseColor("#ff0000"));
            }else {
                finish();
                txvDatos.setText("Nombre: "+sNombre+
                        "\nNúmero tarjeta: "+sNumero+
                        "\nFecha: "+sFecha);
                txvDatos.setTextColor(Color.parseColor("#00e600"));
               // pvMostrarNotificacion1();

            }



        }
    }
    private void pvMostrarNotificacion1() {
        Toast.makeText(this, "Notificacion LENGTH_SHORT", Toast.LENGTH_SHORT).show();
    }
}