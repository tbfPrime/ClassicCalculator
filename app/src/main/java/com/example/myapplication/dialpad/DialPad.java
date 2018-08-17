package com.example.myapplication.dialpad;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by TBF on 06-08-2018.
 */

public class DialPad extends AppCompatActivity {
    private TextView dialPadTextView;
    private String LogTag = "BUTTON";
    String[] keyMap1 = new String[]{"","/","√","OFF","del","%","7","8","9","X","+/-","4","5","6","-","C","1","2","3","+","AC",".","0","="};
    private GridView gridView;
    private RecyclerView keyboard;
    private double bufferNumber = 0;
    private String bufferOperator = "";
    private boolean clearBufferNumber = true;
    private boolean calculatorOFF = false;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(LogTag,"DialPad | onCreate called");
        setContentView(R.layout.dial_pad);

        String[] items = new String[]{"1","2","3","4","5","6","7","8","9","0"};
//        items[1] = "Hello";
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.button,items);

//        gridView = (GridView) findViewById(R.id.keyboard1);
//        gridView.setAdapter(new ButtonAdapter(this,keyMap1));
//        gridView.setAdapter(itemsAdapter);
        keyboard = findViewById(R.id.keyboard1);
        StaggeredGridLayoutManager s = new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL);
        keyboard.setLayoutManager(s);
        keyboard.setAdapter(new KeyBoardAdapter(this,keyMap1));
        dialPadTextView = findViewById(R.id.dialPadInput);
        setDisplayText(String.valueOf(bufferNumber));
        Typeface t = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        dialPadTextView.setTypeface(t);
    //        ViewGroup.LayoutParams l = dialPadTextView.getLayoutParams();
    //        l.width = keyboard.getLayoutParams().width;
//        dialPadTextView.setWidth(keyboard.getWidth());
//        gridView.setAdapter(new ImageAdapter(this));
    }

    public void appendDisplayText(String inputString){
        Log.d("appendDisplayText","Character: " + inputString.toString());
        if((dialPadTextView.getText() == "0") || clearBufferNumber){ clearBufferNumber = false; dialPadTextView.setText(""); }
        String input = dialPadTextView.getText().toString();
        input = input + inputString.toString();
        dialPadTextView.setText(input);
//        dialPadTextView.setText(inputString);
    }
    public void evaluate(){
        double result;
        double secondNumber = Double.parseDouble(dialPadTextView.getText().toString());
        Log.d("DialPad","evaluate | " + bufferNumber + " " + bufferOperator + " " + secondNumber);
        switch (bufferOperator){
            case "+":{
                result = bufferNumber + secondNumber;
                break;
            }
            case "/":{
                result = bufferNumber / secondNumber;
                break;
            }
            case "X":{
                result = bufferNumber * secondNumber;
                break;
            }
            case "-":{
                result = bufferNumber - secondNumber;
                break;
            }
            default:{
                result = 0;
                break;
            }
        }
        setDisplayText(String.valueOf(result));
        clearVariables();
    }
    public void clearVariables(){
        calculatorOFF = false;
        clearBufferNumber = true;
        bufferOperator = "";
        bufferNumber = 0;
    }
    public void clearText(){
        clearVariables();
        setDisplayText(String.valueOf(bufferNumber));
    }
    public void calculatorTurnOFF(){
        Log.d("DialPad","calculatorTurnOFF");
        clearVariables();
        calculatorOFF = true;
        dialPadTextView.setBackground(getResources().getDrawable(R.drawable.dial_pad_background_off));
        setDisplayText("");
    }
    public void calculatorTurnON(){
        Log.d("DialPad","calculatorTurnOn");
        clearVariables();
        calculatorOFF = false;
        dialPadTextView.setBackground(getResources().getDrawable(R.drawable.dial_pad_background));
        setDisplayText(String.valueOf(bufferNumber));
    }
    public void deleteAction(){
        String tempBufferNumber = dialPadTextView.getText().toString();
        Log.d("DialPad","deleteAction | display number: " + tempBufferNumber);
        if(!tempBufferNumber.equals("0")){
            if (tempBufferNumber.length() > 0){
                if (tempBufferNumber.length() > 1){
                    tempBufferNumber = tempBufferNumber.substring(0, (tempBufferNumber.length() - 1));
                }else{
                    tempBufferNumber = "0";
                }
                setDisplayText(tempBufferNumber);
            }
        }
    }
    public void toggleSetNegative(){
        String tempBufferNumber = dialPadTextView.getText().toString();
        if(!tempBufferNumber.equals("0")){
            if(tempBufferNumber.contains("-")){ tempBufferNumber = tempBufferNumber.substring(1,tempBufferNumber.length()); }
            else{ tempBufferNumber = "-" + tempBufferNumber; }
            setDisplayText(tempBufferNumber);
        }
    }
    public String removeDecimalZero(String s){
        if(s.contains(".")){
            String decimalDigits = s.substring((s.indexOf(".") + 1), s.length());
            Log.d("DialPad", "removeDecimalZero | decimalDigits: " + decimalDigits);
            if (decimalDigits.equals("0")) {
                s = s.substring(0, s.indexOf("."));
            }
            Log.d("DialPad", "removeDecimalZero | s: " + s);
        }
        return s;
    }
    public void applyPercentage(){
        if(bufferNumber != 0){
            String tempBufferNumber = dialPadTextView.getText().toString();
            if(!tempBufferNumber.equals("0")){
                tempBufferNumber = String.valueOf((Double.parseDouble(tempBufferNumber) / 100) * bufferNumber);
                setDisplayText(tempBufferNumber);
            }
        }
    }
    private void applySquareRoot(){
        if(!dialPadTextView.getText().toString().isEmpty() && !dialPadTextView.getText().toString().equals("0")){
            String s = String.valueOf(Math.sqrt(Double.parseDouble(dialPadTextView.getText().toString())));
            setDisplayText(s);
        }
    }
    public void setDisplayText(String s){
        if(dialPadTextView != null){ dialPadTextView.setText(removeDecimalZero(s)); }
    }
    public void clickAction(View view, int position, String[] keyMap){
        Log.d("clickAction","view" + view + " | position: " + position);
        Button b = (Button) view;
        if(!(calculatorOFF && !(keyMap[position].equals("AC")))){
            switch(keyMap[position]){
                case "C":{
                    clearText();
                    break;
                }
                case "X":
                case "/":
                case "-":
                case "+":{
                    if(!dialPadTextView.getText().toString().isEmpty()){
                        bufferNumber = Double.parseDouble(dialPadTextView.getText().toString());
                        clearBufferNumber = true;
                        bufferOperator = keyMap[position];
                    }
                    break;
                }
                case "=":{
                    evaluate();
                    break;
                }
                case "":{
                    Log.d("DialPad","clickAction pending.");
                    break;
                }
                case "√":{
                    applySquareRoot();
                    break;
                }
                case "%":{
                    applyPercentage();
                    break;
                }
                case "+/-":{
                    toggleSetNegative();
                    if(bufferOperator.isEmpty()){ bufferNumber = Double.parseDouble(dialPadTextView.getText().toString()); }
                    break;
                }
                case "del":{
                    deleteAction();
                    break;
                }
                case "AC":{
                    calculatorTurnON();
                    break;
                }
                case "OFF":{
                    calculatorTurnOFF();
                    break;
                }
                default:{
                    appendDisplayText(b.getText().toString());
                    Log.d("DialPad","DialPad txt: " + dialPadTextView.getText().toString() + " | bufferNumber: " + bufferNumber + " | int: " + Double.parseDouble(dialPadTextView.getText().toString()) + " | operator empty: " + bufferOperator.isEmpty());
                    break;
                }
            }
        }
    }

    public class ImageAdapter extends BaseAdapter{
        private Context c;
        public ImageAdapter(Context c){
            Log.d("","ImageAdapter contructor called..");
            this.c = c;
        }

        public int getCount(){
            Log.d("","ImageAdapter getCount called..");
            return imgThumbs.length;
        }

        public Object getItem(int position){
            Log.d("","ImageAdapter getItem called..");
            return null;
        }

        public long getItemId(int position){
            Log.d("","ImageAdapter getItemId called..");
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            Log.d("","DialPad | ImageAdapter | getView called.");
            ImageView imageView;
            if(convertView == null){
                imageView = new ImageView(c);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(85,85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8,8,8,8);
            }else{
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(imgThumbs[position]);
            return imageView;
        }

        private Integer[] imgThumbs = {
                R.drawable.image_1, R.drawable.image_2,
                R.drawable.image_3, R.drawable.image_4,
                R.drawable.image_5
        };
    }
    public class ButtonAdapter extends BaseAdapter{
        String[] keyMap;
        private Context c;
        public ButtonAdapter(Context c,String [] keyMap){
            this.c = c;
            this.keyMap = keyMap;
        }
        @Override
        public int getCount(){
            return (keyMap.length > 0) ? keyMap.length : 0;
        }
        @Override
        public View getItem(int position){
            return null;
        }
        @Override
        public long getItemId(int position){
            return 0;
        }
        public View getView(final int position, View view, ViewGroup viewGroup){
            final Button buttonDelegate;
            if(view == null)    {
                buttonDelegate = new Button(c);
                if(keyMap.length > 0){ buttonDelegate.setText(keyMap[position]); }
                else{ Log.e("ButtonAdapter","keyMap not assigned."); }
                buttonDelegate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickAction(view,position,keyMap);
//                        appendDisplayText(buttonDelegate.getText().toString());
                    }
                });
            }else{
                buttonDelegate = (Button) view;
            }
//            if(items[position] == "+"){ buttonDelegate.setLayoutParams(new ViewGroup.LayoutParams(100,200)); }
            return buttonDelegate;
        }
    }

    public class KeyBoardAdapter extends RecyclerView.Adapter<KeyBoardAdapter.KeyboardViewHolder>{
        private Context c;
        private String[] keyMap;
        public KeyBoardAdapter(Context c, String[] keyMap){
            Log.d("KeyBoardAdapter","KeyBoardAdapter | c: " + c + " | keyMap: " + keyMap);
            this.c = c;
            this.keyMap = keyMap;
        }
        @Override
        public void onBindViewHolder    (KeyboardViewHolder keyboardViewHolder, final int position){
            Log.d("KeyBoardAdapter","onBindViewHolder | keyboardViewHolder: " + keyboardViewHolder + " | position: " + position + " | B: " + keyboardViewHolder.b);
            keyboardViewHolder.b.setText(keyMap[position]);
//            int width = keyboardViewHolder.v.getMeasuredWidth();
//            int height = keyboardViewHolder.v.getMeasuredHeight() / 5;
//            keyboardViewHolder.b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ViewGroup.LayoutParams l = keyboardViewHolder.b.getLayoutParams();
            if(keyMap[position] == "+"){
                l.width = 100;
                l.height = 200; //keyboardViewHolder.v.getMeasuredHeight() / 4;
            }else{
                l.width = 100;
                l.height = 100;//keyboardViewHolder.v.getMeasuredHeight() / 5;
            }
//            Color c = new Color;
            keyboardViewHolder.b.setBackground(getResources().getDrawable(((keyMap[position] == "C") || (keyMap[position] == "AC")) ? R.drawable.button_red : R.drawable.button_grey));
            keyboardViewHolder.b.setTextColor(getResources().getColor(R.color.buttonTextColor));
//            if(keyMap[position] == "3"){ keyboardViewHolder.b.setLayoutParams(l); }
            keyboardViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    clickAction(view,position,keyMap);
                }
            });
        }
        @Override
        public int getItemCount(){
            Log.d("KeyBoardAdapter","getItemCount | keyMap.length: " + keyMap.length);
            return (keyMap.length > 0) ? keyMap.length : 0;
        }
        @Override
        public KeyboardViewHolder onCreateViewHolder(ViewGroup parent, int position){
            Log.d("KeyBoardAdapter","onCreateViewHolder | parent: " + parent + " | position: " + position);
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.button,parent,false);
//
//            int height = parent.getMeasuredHeight();
//            int width = parent.getMeasuredWidth();
//
//            v.setLayoutParams(new ViewGroup.LayoutParams(width,height));

            KeyboardViewHolder d = new KeyboardViewHolder(v,position,parent);
            return d;
        }
        public class KeyboardViewHolder extends RecyclerView.ViewHolder{
            Button b;
            ViewGroup v;
            public void setText(String s){
                Log.d("KeyboardViewHolder","setText | s: " + s);
                b.setText(s);
            }
            public KeyboardViewHolder(View itemView, int position, ViewGroup v){
                super(itemView);
                Log.d("KeyboardViewHolder","KeyboardViewHolder called.");
                b = itemView.findViewById(R.id.button);
                this.v = v;
//                b.setLayoutParams(new ViewGroup.LayoutParams(150,50));
//                if(keyMap[position] == "3"){ b.setLayoutParams(new ViewGroup.LayoutParams(150,50)); }
            }
        }
    }
}
