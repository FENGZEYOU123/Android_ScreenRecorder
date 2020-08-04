package com.example.yfz_screenrecorder;

import android.app.Activity;
import android.app.MediaRouteButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 *
 * 实现RecyclerView的方法
 *
 **/
public class MyRecyclerView extends RecyclerView.Adapter<MyRecyclerView.ViewHolder>{     //extends recyclerview.adapter
    MainActivity MainActivity= new MainActivity();
    Data Data = (Data) MainActivity.context;
     private static String TAG= "MyRecyclerView:   ";

        private List mLIst;
          MyRecyclerView(List list){
            mLIst=list;
        }


        //创建ViewHolder并返回，后续item布局里控件都是从ViewHolder中取出
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //将我们自定义的item布局R.layout.listview_item转换为View
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
            //将view传递给我们自定义的ViewHolder
            ViewHolder holder = new ViewHolder(view);
            //返回这个MyHolder实体
            return holder;
        }


        //通过方法提供的ViewHolder，将数据绑定到ViewHolder中
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.list_item_textview.setText(""+mLIst.get(position));  //展示文件名字
            holder.list_item_img.setOnClickListener(new img_button(position));  //添加点击事件，并将position的值传递进去
        }

        //获取数据源总的条数
        @Override
        public int getItemCount() {
            return mLIst.size();
        }

        //创建ViewHolder并返回，后续item布局里控件都是从ViewHolder中取出

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageButton list_item_img;
            TextView list_item_textview,list_item_img_textview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                list_item_img=   itemView.findViewById(R.id.list_item_img);
                list_item_img_textview=itemView.findViewById(R.id.list_item_img_textview);
                list_item_textview= itemView.findViewById(R.id.list_item_textview);
            }
        }

        //处理点击事件
        public class img_button implements View.OnClickListener {
            int position;

            public img_button(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View v) {
            Log.e("MyRecyclerView", "点击: "    +Data.getRootDir()+mLIst.get(position));
            MainActivity.surfaceView_layout.setVisibility(View.VISIBLE);
             }
        }





    }






