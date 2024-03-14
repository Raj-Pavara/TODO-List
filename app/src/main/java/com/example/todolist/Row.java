package com.example.todolist;

public class Row {
    int id;
    String  _Title;
    String _Discription;

    int checked;

    Row(int id, String title, String discription,int checked){
        this.id = id;
        this._Title = title;
        this._Discription = discription;
        this.checked = checked;                        // checked = 0  checkbox false  checked = 1 checkbox true.
    }
    Row(){

    }
}
