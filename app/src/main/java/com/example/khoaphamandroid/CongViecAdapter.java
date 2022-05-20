package com.example.khoaphamandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CongViecAdapter extends BaseAdapter {
    private MainActivity context;
    private int layout;
    private List<CongViec> congViecList;

    public CongViecAdapter(MainActivity context, int layout, List<CongViec> congViecList) {
        this.context = context;
        this.layout = layout;
        this.congViecList = congViecList;
    }

    @Override
    public int getCount() {
        return congViecList.size();
    }

    private class ViewHolder{
        TextView txtTen;
        ImageView imgDel, imgEdit;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Gán layout bằng biến
            view = inflater.inflate(layout, null);
            //ánh xạ
            holder.txtTen = (TextView) view.findViewById(R.id.txt_ten);
            holder.imgDel = (ImageView) view.findViewById(R.id.iv_del);
            holder.imgEdit = (ImageView) view.findViewById(R.id.iv_edit);
            //nhớ gọi setTag để nó ánh xạ qua
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        CongViec congViec = congViecList.get(i);
        holder.txtTen.setText(congViec.getTenCV());

        //bắt sự kiện xóa và sửa
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Sửa " + congViec.getTenCV(), Toast.LENGTH_SHORT).show();
                context.dialogSua(congViec.getIdCV() ,congViec.getTenCV());
            }
        });

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.xoaCv(congViec.getIdCV() ,congViec.getTenCV());
            }
        });
        return view;
    }
}
