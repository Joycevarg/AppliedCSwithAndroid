package com.google.engedu.wordladder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.engedu.worldladder.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class SolverActivity extends AppCompatActivity {

    Integer noOfPaths,pathlength;
    ArrayList<ArrayList<String>> paths;
    TextView tv;
    ArrayList<HashSet<String>> validValues;
    LinearLayout linearLayout;
    String startString,endString;
    ArrayList<Boolean> correct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_solver);
        TextView start=findViewById(R.id.startTextView);
        TextView end=findViewById(R.id.endTextView);
        Intent intent = getIntent();
        tv=findViewById(R.id.congrats);
        tv.setVisibility(View.INVISIBLE);
        noOfPaths=intent.getIntExtra("noOfPaths",0);
        pathlength=intent.getIntExtra("pathLength",0);
        startString=intent.getStringExtra("Start");
        endString=intent.getStringExtra("End");
        paths=new ArrayList<>();
        validValues=new ArrayList<>();
        correct=new ArrayList<>();
        for(int j=1;j<pathlength-1;j++)
        {
            HashSet<String> hs=new HashSet<>();
            validValues.add(hs);
        }

        for(int i=0;i<noOfPaths;i++)
        {
            ArrayList<String> path=intent.getStringArrayListExtra("dict"+i);
            paths.add(path);
            for(int j=1;j<pathlength-1;j++)
            {
                validValues.get(j-1).add(path.get(j));
            }

        }

        start.setText(startString);
        end.setText(endString);
        linearLayout = findViewById(R.id.act_cont);
        // Create EditText
        EditText editText;
        for(int i=1;i<pathlength-1;i++)
        {
            correct.add(false);
            editText = new EditText(this);
            editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            editText.setPadding(20, 20, 20, 20);
            linearLayout.addView(editText,i);
            editText.addTextChangedListener(new inputWatcher(editText));

        }

    }

    int firstWrong()
    {
        for(int i=0;i<correct.size();i++)
            if(!correct.get(i))
                return i;
            return 0;
    }

    private class inputWatcher implements TextWatcher{

        private View view;
        private inputWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String input=editable.toString();
            Integer index=linearLayout.indexOfChild(view)-1;
            EditText et=(EditText)view;
            if(validWord(input,index)) {
                correct.set(index,true);
                if(index!=0)
                {
                    linearLayout.getChildAt(index-1);

                }
                et.setTextColor(Color.GREEN);
            }
            else {
                correct.set(index,false);
                if(index!=0)
                {
                    linearLayout.getChildAt(index-1).setFocusable(true);
                }
                et.setTextColor(Color.RED);
            }
        }
    }
    public boolean validWord(String input,int index)
    {
        if(index==0)
        {
//            String before=((EditText)linearLayout.getChildAt(index-1)).getText().toString();
            if(PathDictionary.areNeighbours(startString,input)&&validValues.get(index).contains(input))
                return true;
        }
        else
        {
            String before=((EditText)linearLayout.getChildAt(index)).getText().toString();
            if(PathDictionary.areNeighbours(before,input)&&validValues.get(index).contains(input)) {
                if (index == pathlength - 3) {
                    tv.setVisibility(View.VISIBLE);
                }
                return true;
            }
            else if(index == pathlength - 3)
            {
                tv.setVisibility(View.INVISIBLE);
            }
        }
        return false;
    }

    public void onNext(View v)
    {
        Intent intent = new Intent(this, WordSelectionActivity.class);
        startActivity(intent);
    }


    public void onSolve(View v)
    {
        linearLayout = findViewById(R.id.act_cont);
        EditText edtext;
        int fw=firstWrong();
        int pathno=0;
        if(fw!=0)
        {

            String lastcorrect=((EditText)linearLayout.getChildAt(fw)).getText().toString();


            for(int i=0;i<noOfPaths;i++)
            {
                String check=paths.get(i).get(fw);
                if(check.equals(lastcorrect))
                {
                    pathno=i;break;
                }
            }
        }


        for(int i=0;i<pathlength-2;i++)
        {
            edtext=(EditText)linearLayout.getChildAt(i+1);
            edtext.setText(paths.get(pathno).get(i+1));
        }
    }
}
