package tw.org.iii.myuploadtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private File sdroot;
    private ProgressDialog pDialog;
    private UIHandler handler;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView)findViewById(R.id.img);

        sdroot = Environment.getExternalStorageDirectory();

        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setTitle("Downloading...");

        handler = new UIHandler();

    }

    public void newFile(View v){
        File newFile = new File(sdroot.getAbsolutePath() + "/DK.txt");
        try {
            FileOutputStream fout =
                    new FileOutputStream(newFile);
            fout.write("Hello, World".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"Write OK", Toast.LENGTH_SHORT).show();
        }catch (Exception ee){

        }
    }

    public void upload(View v){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    MultipartUtility mu =
                            new MultipartUtility("http://www.brad.tw/iii2003/upload.php", "UTF-8");
                    mu.addFilePart("upload", new File(sdroot.getAbsolutePath() + "/DK.txt"));
                    List<String> ret = mu.finish();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void test1(View v){
        pDialog.show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    MultipartUtility mu =
                            new MultipartUtility("http://data.coa.gov.tw/Service/OpenData/EzgoAttractions.aspx", "UTF-8");
                    List<String> ret = mu.finish();
                    for (String line : ret){
                        Log.v("DK", line.length() + ":" + line);
                    }
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    public void camera(View v){
        Intent it = new Intent(this, MycameraActivity.class);
        startActivityForResult(it,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        byte[] pic = data.getByteArrayExtra("pic");
        img.setImageBitmap(BitmapFactory.decodeByteArray(pic,0,pic.length));
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pDialog.dismiss();
        }
    }

}