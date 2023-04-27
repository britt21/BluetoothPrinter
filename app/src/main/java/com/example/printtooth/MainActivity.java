package com.example.printtooth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

Button printbtn,unpairbtn;
Printing  printing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    void initView(){
        printbtn  = findViewById(R.id.printbtn);
        unpairbtn  = findViewById(R.id.unpairbtn);

        if ((printing != null)){

            unpairbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Printooth.INSTANCE.hasPairedPrinter()){
                        Printooth.INSTANCE.removeCurrentPrinter();
                    }else {
                        startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                        changePairandUnpair();
                    }
                }
            });

            printbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Printooth.INSTANCE.hasPairedPrinter()){
                        startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                    }else {
                        printText();
                    }
                }
            });
            printing.setPrintingCallback(new PrintingCallback() {
                @Override
                public void connectingWithPrinter() {

                    Toast.makeText(MainActivity.this,"Connecting",Toast.LENGTH_LONG).show();
                }

                @Override
                public void printingOrderSentSuccessfully() {
                    Toast.makeText(MainActivity.this,"Order Sent to printer",Toast.LENGTH_LONG).show();

                }

                @Override
                public void connectionFailed(@NonNull String s) {

                    Toast.makeText(MainActivity.this,"failed"+ s,Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(@NonNull String s) {

                    Toast.makeText(MainActivity.this,"Error" + s,Toast.LENGTH_LONG).show();

                }

                @Override
                public void onMessage(@NonNull String s) {
                    Toast.makeText(MainActivity.this,"Message" + s,Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void printText() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(new RawPrintable.Builder(new byte[]{27,100,4}).build());

        printables.add(new TextPrintable.Builder()
                .setText("Hello Brigh/ Umar" + "Hi welcome")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());


        printables.add(new TextPrintable.Builder()
                .setText("Thursday")
                .setCharacterCode(DefaultPrinter.Companion.getLINE_SPACING_60())
                        .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                        .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setNewLinesAfter(1)
                .build());

        printing.print(printables);


    }

    private void changePairandUnpair() {
        if (Printooth.INSTANCE.hasPairedPrinter()){
            unpairbtn.setText("unpair");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER &&
            resultCode == Activity.RESULT_OK){
            initPrinting();
        }
    }

    private void initPrinting() {
        if (!Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();
            if (printing != null){
                printing.setPrintingCallback((PrintingCallback) this);
            }
    }
}