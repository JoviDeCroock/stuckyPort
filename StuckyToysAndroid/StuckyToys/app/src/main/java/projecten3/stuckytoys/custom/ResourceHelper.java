package projecten3.stuckytoys.custom;

import java.lang.reflect.Field;

import projecten3.stuckytoys.R;

public abstract class ResourceHelper {

    //Class<?> c -> the sort of resource you want to find; resName -> name of the resource you want
    //example: bever.png -> getResId("bever.png", R.drawable.class)
    public static int getResId(String resName, Class<?> c) {
        try {
            //split in case of images; gets rid of .png (usually) or whatever cause that isn't part of the resource id
            Field idField = c.getDeclaredField(resName.split("\\.")[0]);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
