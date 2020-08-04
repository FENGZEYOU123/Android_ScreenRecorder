package com.example.yfz_screenrecorder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

/**
 *
 * 实现RecyclerView的方法
 *
 **/
public class MyRecyclerView extends RecyclerView.Adapter<MyRecyclerView.ViewHolder>{     //extends recyclerview.adapter
    MainActivity MainActivity= new MainActivity();
    data Data = (data) MainActivity.context;
     private static String TAG= "MyRecyclerView:   ";

        private List My_mLIst;
          MyRecyclerView(List list){
            My_mLIst=list;
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
            holder.list_item_textview.setText(""+My_mLIst.get(position));  //展示文件名字
            holder.list_item_img.setOnClickListener(new play_button(position));  //添加点击事件，并将position的值传递进去
            holder.list_item_img_delete.setOnClickListener(new delete_button(position,MainActivity.context));
        }

        //获取数据源总的条数
        @Override
        public int getItemCount() {
            return My_mLIst.size();
        }

        //创建ViewHolder并返回，后续item布局里控件都是从ViewHolder中取出

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageButton list_item_img,list_item_img_delete;
            TextView list_item_textview,list_item_img_textview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                list_item_img=   itemView.findViewById(R.id.list_item_img);
                list_item_img_delete=itemView.findViewById(R.id.list_item_img_delete);
                list_item_img_textview=itemView.findViewById(R.id.list_item_img_textview);
                list_item_textview= itemView.findViewById(R.id.list_item_textview);
            }
        }

        //处理播放点击事件
        public class play_button implements View.OnClickListener {
            int position;

            public play_button(int position) {
                this.position = position;
            }
            @Override
            public void onClick(View v) {
            Log.e("MyRecyclerView", "play_button点击:   "+position+"    "    +Data.getRootDir()+My_mLIst.get(position));
            MainActivity.surfaceView_layout.setVisibility(View.VISIBLE);
             }
        }

        //处理删除点击事件
    public class delete_button implements View.OnClickListener {
        int position;
        Context context;

        public delete_button(int position,Context context) {
            this.position = position;
            this.context = context;

        }
        @Override
        public void onClick(View v) {
            String file_path=Data.getRootDir()+My_mLIst.get(position);
            Log.e("MyRecyclerView", "delete_button点击: "  +position+"    "+file_path);
            File file= new File(file_path);
            file.delete();
            MainActivity.mList.remove(position);
            //发送广播通知更新recycleview的ui
            Intent aaa=new Intent("update");
            aaa.putExtra("update",true);
            context.sendBroadcast(aaa);

            }
    }




    private  boolean  deleteFiles(File file){
        try{
            if(file.isDirectory()){ //判断是否是文件夹
                File[] files = file.listFiles();//遍历文件夹里面的所有的
                for(int i=0;i<files.length;i++){
                    Log.e(TAG, "删除文件>>>>>> "+files[i].toString());
                    deleteFiles(files[i]); //删除
                }
            }else{
                file.delete();
            }
            System.gc();//系统回收垃圾
            return true;
        }catch (Exception e){
            Log.e(TAG, "删除报错！！！: "+e.toString());
            return false;

        }

    }


    }






