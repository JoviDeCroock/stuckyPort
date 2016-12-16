package projecten3.stuckytoys.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import projecten3.stuckytoys.domain.WidgetFile;
import projecten3.stuckytoys.fragments.SceneFragment;
import projecten3.stuckytoys.fragments.StoryListFragment;
import projecten3.stuckytoys.persistence.PersistenceController;

public class DownloadSoundTask extends AsyncTask<WidgetFile, Integer, Boolean> {
    String downloadUrl = PersistenceController.BASEURL + "downloads/Geluid/";
    private StoryListFragment context;

    public DownloadSoundTask(StoryListFragment context) {
        this.context = context;
    }

    protected Boolean doInBackground(String... paths) {
        int count = paths.length;
        List<byte[]> allImages = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                URL url = new URL(downloadUrl + paths[i]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                allImages.add(byteArray);
            } catch (IOException e) {
                // Log exception
                return false;
            }
        }
        return true;
    }

    protected Boolean doInBackground(WidgetFile... files) {
        int count = files.length;

        for (int i = 0; i < count; i++) {
            try {
                URL url = new URL(downloadUrl + files[i].getFileName());
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lengthOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream output = new ByteArrayOutputStream();

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1)
                {
                    output.write(buffer, 0, bytesRead);
                }
                files[i].setBytes(output.toByteArray());

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    protected void onPostExecute(Boolean result) {
        context.updateWidgetSounds(result);
    }
}
