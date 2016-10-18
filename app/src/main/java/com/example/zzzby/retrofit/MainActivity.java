package com.example.zzzby.retrofit;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  使用Retrofit网络加载框架实现文件的下载并显示进度
 *
 */
public class MainActivity extends AppCompatActivity {

    private String url="http://sc.jb51.net/uploads/allimg/150703/14-150F3102113Y7.jpg";
    private ImageView mainImageView;
    private Button downloadBtn;
    private TextView progressTextView;

    private Retrofit retrofit;
    private IRetrofitApi iRetrofitApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRetrofit();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl("http://www.baidu.com/").build();
        iRetrofitApi = retrofit.create(IRetrofitApi.class);
    }

    private void initView() {
        mainImageView = (ImageView) findViewById(R.id.iv_main);
        progressTextView = (TextView) findViewById(R.id.tv_progress);
        downloadBtn = (Button) findViewById(R.id.btn_download);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask().execute();
            }
        });
    }
    class DownloadTask extends AsyncTask<Void,Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Call<ResponseBody> bodyCall = iRetrofitApi.downloadFile(url);
            Bitmap bitmap = null;
            try {
                retrofit2.Response<ResponseBody> response = bodyCall.execute();
                //总长度
                long sumLength = response.body().contentLength();

                //下载长度
                long currentLength = 0;
                InputStream inputStream = response.body().byteStream();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"fengjing.png");
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] array = new byte[1024];
                int len = 0 ;
                while ((len = inputStream.read(array))!=-1){
                    outputStream.write(array,0,len);
                    currentLength += len;
                    //当前进度
                    int progress = (int) ((currentLength * 100) / sumLength);
                    publishProgress(progress);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int value = values[0];
            if (value==100){
                progressTextView.setText("下载完成");
            }
            progressTextView.setText("下载进度(%)" + String.valueOf(value));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mainImageView.setImageBitmap(bitmap);
        }
    }
}
