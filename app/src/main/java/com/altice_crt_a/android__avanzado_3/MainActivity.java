package com.altice_crt_a.android__avanzado_3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MyBeautifulService.ServiceChangeListener {

    EditText numberInput;
    TextView numberResultText;
    Button isPrimeButton;
    LinearLayout scrollViewLayout;
    Looper looper;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(MyBeautifulService.getInstance() == null) {
            Intent serviceIntent = new Intent(this, MyBeautifulService.class);
            startService(serviceIntent);

        }


        numberInput = findViewById(R.id.number_input);
        isPrimeButton = findViewById(R.id.number_button);
        numberResultText = findViewById(R.id.main_result_text);
        scrollViewLayout = findViewById(R.id.scroll_linear_layout);

        looper = Looper.getMainLooper();
        handler = new Handler();

        isPrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberInput.getText().length() > 0) {
                    numberResultText.setVisibility(View.VISIBLE);
                    numberResultText.setText("Calculating...");
                    scrollViewLayout.removeAllViews();
                    //new PrimeCalculatorTask().execute(Integer.parseInt(numberInput.getText().toString()));
                    handler.postDelayed(runnable,100);

                }
                else {
                    numberInput.setError("Enter a number, you beast.");
                }
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {

                Integer integers =Integer.parseInt(numberInput.getText().toString()) ;

                if(integers == 0 || integers == 1) {
                    numberResultText.setText("Number " + integers.toString() + " Is not prime.");
                }
                if(integers > 2) {
                    TextView tv = new TextView(MainActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0, 15);
                    tv.setLayoutParams(params);
                    tv.setText("2");
                    scrollViewLayout.addView(tv);
                }
                //Even numbers can't be prime so...
                for(int i = 3; i < integers; i+=2) {
                    if(isPrime(i)) {
                        TextView tv = new TextView(MainActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,0, 15);
                        tv.setLayoutParams(params);
                        tv.setText(Integer.toString(i));
                        scrollViewLayout.addView(tv);
                    }
                }
                if(isPrime(integers)) {
                    numberResultText.setText("Number " + integers.toString() + " Is prime.");
                }
                else {
                    numberResultText.setText("Number " + integers.toString() + " Is not prime.");
                }
            }
        };

    }



    private boolean isPrime(Integer n) {
        int sqRoot = (int) Math.sqrt(n.doubleValue());
        for(int i = 2; i <= sqRoot; i++) {
            if(n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

       // MyBeautifulService.getInstance().addChangeListener(this);
    }

    @Override
    public void onChange() {
        Log.d("SERVICE_TIMER", "IM SO HOOOOOOOT");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyBeautifulService.getInstance().removeChangeListener(this);
    }



    public class PrimeCalculatorTask extends AsyncTask<Integer, Integer, Boolean>{
        private Integer lastN;

        private boolean isPrime(Integer n) {
            int sqRoot = (int) Math.sqrt(n.doubleValue());
            for(int i = 2; i <= sqRoot; i++) {
                if(n % i == 0) {
                    return false;
                }
            }
            return true;
        }
        @Override
        protected Boolean doInBackground(Integer... integers) {
            lastN = integers[0];

            if(integers[0] == 0 || integers[0] == 1) {
                return false;
            }
            if(integers[0] >= 2) {
               publishProgress(2);
            }
            //Even numbers can't be prime so...
            for(int i = 3; i < integers[0]; i+=2) {
                if(isPrime(i)) {
                    publishProgress(i);
                }
            }
            return isPrime(integers[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            TextView tv = new TextView(MainActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0, 15);
            tv.setLayoutParams(params);
            tv.setText(values[0].toString());
            scrollViewLayout.addView(tv);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                //If the final number (N) is a boolean, we'll show it.
                numberResultText.setText("Number " + lastN.toString() + " Is prime.");
            }
            else {
                numberResultText.setText("Number " + lastN.toString() + " Is not prime.");
            }
        }
    }
}
