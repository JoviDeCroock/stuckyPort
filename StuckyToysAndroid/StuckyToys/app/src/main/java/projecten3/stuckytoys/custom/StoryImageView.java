package projecten3.stuckytoys.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class StoryImageView extends ImageView {
    public StoryImageView(Context context) {
        super(context);
    }

    public StoryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoryImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}