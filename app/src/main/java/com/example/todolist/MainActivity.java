package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private FloatingActionButton FAB;
    private androidx.appcompat.widget.AppCompatButton appCompatButton;
    private GridView gridView;
    private LinearLayout linearLayout;
    private DBHelper dbh;
    private GridAdapter gridAdapter;
    private ArrayList<Row> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initlization();

        setListenerMethods();


    }

    public void initlization() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FAB = findViewById(R.id.floatingActionButton);
        appCompatButton = findViewById(R.id.appCompatButton);
        gridView = findViewById(R.id.gridView);
        linearLayout = findViewById(R.id.linearLayout);
        dbh = new DBHelper(MainActivity.this);
        data = dbh.getData();
        gridAdapter = new GridAdapter(this, data);
        gridView.setAdapter(gridAdapter);
        if (data.size() == 0) {
            gridView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    public void setListenerMethods() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNotesDialog();

            }
        };

        FAB.setOnClickListener(listener);
        appCompatButton.setOnClickListener(listener);
    }


    public void addNotesDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();

        Button addButton = dialog.findViewById(R.id.addButton);
        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText1 = dialog.findViewById(R.id.editText1);
                        EditText editText2 = dialog.findViewById(R.id.editText2);
                        String str1 = editText1.getText().toString();
                        String str2 = editText2.getText().toString();
                        CheckBox checkBox = dialog.findViewById(R.id.checkBox);

                        if (!(str1.equals("") || str2.equals(""))) {

                            SharedPreferences sp = getSharedPreferences("demo", MODE_PRIVATE);
                            int last = sp.getInt("last", 1);

                            Row r ;
                            if(checkBox.isChecked()){
                                r = new Row(last+1,str1,str2,1);
                            }
                            else{
                                r = new Row(last +1 , str1,str2,0);
                            }
                            dbh.insert(r);
                            linearLayout.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);

                            data = dbh.getData();
                            gridAdapter.function(data);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("last", last + 1);
                            editor.commit();
                            Toast.makeText(MainActivity.this, "Task added sucesfully!!", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter full details!!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
        );
    }



    public void deleteCard(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure , you went to delete this list !!!");
        builder.setCancelable(true);
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbh.deleteRow(data.get(position).id);
                        data = dbh.getData();
                        gridAdapter.function(data);
                        Toast.makeText(getApplicationContext(), "Task deleted sucessfully!!", Toast.LENGTH_SHORT).show();
                        if (data.size() == 0) {
                            linearLayout.setVisibility(View.VISIBLE);
                            gridView.setVisibility(View.GONE);
                        }
                    }
                });

        builder.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Delete List!");
        alert.show();

    }

    public void updateNotesDialog(int position) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();

        Row row = data.get(position);
        EditText editText1 = dialog.findViewById(R.id.editText1);
        EditText editText2 = dialog.findViewById(R.id.editText2);
        editText1.setText(row._Title);
        editText2.setText(row._Discription);
        Button addButton = dialog.findViewById(R.id.addButton);
        CheckBox checkBox = dialog.findViewById(R.id.checkBox);
        if(row.checked == 0){
            checkBox.setChecked(false);
        }
        else{
            checkBox.setChecked(true);
        }
        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str1 = editText1.getText().toString();
                        String str2 = editText2.getText().toString();


                        if (!(str1.equals("") || str2.equals(""))) {
                            Row r ;
                            if(checkBox.isChecked()){
                                r = new Row(row.id,str1,str2,1);
                            }
                            else{
                                r = new Row(row.id,str1,str2,0);
                            }
                            dbh.updataRow(r);
                            data = dbh.getData();
                            gridAdapter.function(data);

                            Toast.makeText(MainActivity.this, "Task edited succesfully!!", Toast.LENGTH_SHORT).show();

                        }

                        dialog.cancel();

                    }

                }
        );
    }

    public void editCheckBox(int position){
        Row row = data.get(position);
        Row r;
        if(row.checked == 1){
            r = new Row(row.id,row._Title,row._Discription,0);
        }
        else{
            r = new Row(row.id,row._Title,row._Discription,1);
        }
        dbh.updataRow(r);
        data = dbh.getData();
        gridAdapter.function(data);
    }



    class GridAdapter extends ArrayAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<Row> data = new ArrayList<>();

        int colors[];

        public GridAdapter(Context context, ArrayList<Row> data) {
            super(context, R.layout.gridview_layout);
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;

            colors = new int[]{
                    context.getResources().getColor(R.color.color1),
                    context.getResources().getColor(R.color.color2),
                    context.getResources().getColor(R.color.color3),
                    context.getResources().getColor(R.color.color4),
                    context.getResources().getColor(R.color.color5),
                    context.getResources().getColor(R.color.color6),
                    context.getResources().getColor(R.color.color7),
                    context.getResources().getColor(R.color.color8),
                    context.getResources().getColor(R.color.color9),
                    context.getResources().getColor(R.color.color10),
                    context.getResources().getColor(R.color.color11),
                    context.getResources().getColor(R.color.color12)};
        }

        @Override
        public int getCount() {

            return data.size();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.gridview_layout, null);
            TextView textView1 = view.findViewById(R.id.textView1);
            TextView textView2 = view.findViewById(R.id.textView2);
            textView1.setText(data.get(position)._Title);
            textView2.setText(data.get(position)._Discription);
            CheckBox checkBox = view.findViewById(R.id.checkBox2);
            if(data.get(position).checked == 0){
                checkBox.setChecked(false);
            }
            else{
                checkBox.setChecked(true);
            }

            checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                         editCheckBox(position);
                        }
                    }
            );
            ImageButton ib_delete = view.findViewById(R.id.imageButton_delete);
            ib_delete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteCard(position);
                        }
                    }
            );

            ImageButton ib_edit = view.findViewById(R.id.imageButton_edit);
            ib_edit.setBackgroundColor(colors[position%12]);
            ib_delete.setBackgroundColor(colors[position%12]);
            ib_edit.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            updateNotesDialog(position);
                        }
                    }
            );


            CardView cardView = view.findViewById(R.id.cardView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setCardBackgroundColor(colors[position % 12]);
            }

            return view;
        }

        public void function(ArrayList<Row> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }
}