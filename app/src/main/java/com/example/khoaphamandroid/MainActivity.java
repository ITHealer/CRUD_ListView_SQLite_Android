package com.example.khoaphamandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
//good luck!
    private Database db;
    private ListView lvCongViec;
    private List<CongViec> arrCongViec;
    CongViecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = (ListView) findViewById(R.id.lv_congviec);

        // khởi tạo database
        db = new Database(MainActivity.this);
        extracted(db.getEveryone());

    }

    private void extracted(List<CongViec> arrCongViec) {
        adapter = new CongViecAdapter(this, R.layout.item_cong_viec, arrCongViec);
        lvCongViec.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_congviec, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Cách 1:
                ArrayList<CongViec> arrCV = new ArrayList<>();
                Cursor cursor = db.searchUsers(s);
                //arrCV.clear();
                while (cursor.moveToNext()){
                    int IdCV = cursor.getInt(0);
                    String tencv = cursor.getString(1);
                    arrCV.add(new CongViec(IdCV, tencv));
                }
                CongViecAdapter adapterSearch = new CongViecAdapter(MainActivity.this, R.layout.item_cong_viec, arrCV);
                lvCongViec.setAdapter(adapterSearch);
                adapterSearch.notifyDataSetChanged();
                return true;

                //Cách 2:
//                ArrayList<CongViec> arrCV = new ArrayList<>();
//                for (CongViec item : arrCongViec) {
//                    if(item.getTenCV().contains(s)){
//                        arrCV.add(item);
//                    }
//                }
//                CongViecAdapter adapterSearch = new CongViecAdapter(MainActivity.this, R.layout.item_cong_viec, arrCV);
//                lvCongViec.setAdapter(adapterSearch);
//                adapterSearch.notifyDataSetChanged();
//                return true;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //bắt sự kiện trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_add:
                dialogThem();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogThem(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);

        EditText edtTen = (EditText) dialog.findViewById(R.id.edit_tencv);
        Button btnThem = (Button) dialog.findViewById(R.id.btn_them);
        Button btnHuy = (Button) dialog.findViewById(R.id.btn_huy);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialogTen = edtTen.getText().toString().trim();
                if(dialogTen.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
                }else {
                    //db.QueryData("INSERT INTO CongViec VALUES(null, '"+ dialogTen +"')");
                    boolean sussess = db.addCongViec(dialogTen);
                    Toast.makeText(MainActivity.this, "Đã thêm!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    extracted(db.getEveryone());
                }
            }
        });

        //setCanceledOnTouchOutside khi chạm bên ngoài dialog sẽ k bị tắt; mặc định là true
        dialog.setCanceledOnTouchOutside(false);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialogSua(int id, String tencv){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_cong_viec);

        EditText edtTen = (EditText) dialog.findViewById(R.id.edit_tencv);
        Button btnCapNhat = (Button) dialog.findViewById(R.id.btn_sua);
        Button btnHuy = (Button) dialog.findViewById(R.id.btn_huy);

        edtTen.setText(tencv);
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialogTenMoi = edtTen.getText().toString().trim();
                //db.QueryData("UPDATE CongViec SET TenCV = '"+ dialogTenMoi +"' WHERE Id = '"+ id +"'");
                db.UpdateCongViec(id, dialogTenMoi);
                Toast.makeText(MainActivity.this, "Đã sửa!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                extracted(db.getEveryone());
            }
        });

        //setCanceledOnTouchOutside khi chạm bên ngoài dialog sẽ k bị tắt; mặc định là true
        dialog.setCanceledOnTouchOutside(false);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void xoaCv(final int id, String tenCV){
        AlertDialog.Builder diaBuilder = new AlertDialog.Builder(this);
        diaBuilder.setMessage("Bạn có chắc chắn muốn xóa?");
        diaBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //db.QueryData("DELETE FROM CongViec WHERE Id = '"+ id +"'");
                db.deleteCongViec(tenCV);
                Toast.makeText(MainActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                extracted(db.getEveryone());
            }
        });
        diaBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        diaBuilder.show();
    }
}