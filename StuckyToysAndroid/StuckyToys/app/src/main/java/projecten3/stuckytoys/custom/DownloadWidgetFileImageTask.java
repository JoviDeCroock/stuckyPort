package projecten3.stuckytoys.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import projecten3.stuckytoys.domain.WidgetFile;
import projecten3.stuckytoys.fragments.StoryListFragment;
import projecten3.stuckytoys.persistence.PersistenceController;

public class DownloadWidgetFileImageTask extends AsyncTask<WidgetFile, Integer, Boolean> {
    String downloadUrl = PersistenceController.BASEURL + "downloads/Afbeelding/";
    private StoryListFragment context;
    private List<WidgetFile> soundWidgetFiles;

    public DownloadWidgetFileImageTask(StoryListFragment context, List<WidgetFile> soundWidgetFiles) {
        this.context = context;
        this.soundWidgetFiles = soundWidgetFiles;
    }

    protected Boolean doInBackground(WidgetFile... files) {
        int count = files.length;
        for (int i = 0; i < count; i++) {
            try {
                URL url = new URL(downloadUrl + files[i].getFileName());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                files[i].setBytes(byteArray);
            } catch (IOException e) {
                // Log exception
                return false;
            }
        }
        return true;
    }

    protected void onPostExecute(Boolean result) {
        context.updateWidgetImages(result, soundWidgetFiles);
    }
}