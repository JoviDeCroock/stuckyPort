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

import projecten3.stuckytoys.fragments.StoryListFragment;
import projecten3.stuckytoys.persistence.PersistenceController;

public class DownloadImageTask extends AsyncTask<String, Integer, List<byte[]>> {
    String downloadUrl = PersistenceController.BASEURL + "downloads/Afbeelding/";
    private StoryListFragment context;

    public DownloadImageTask(StoryListFragment context) {
        this.context = context;
    }

    protected List<byte[]> doInBackground(String... paths) {
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
                return null;
            }
        }
        return allImages;
    }

    protected void onPostExecute(List<byte[]> result) {
        context.updateStoryImages(result);
    }
}
