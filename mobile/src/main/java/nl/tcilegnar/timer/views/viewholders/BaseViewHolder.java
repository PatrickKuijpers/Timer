package nl.tcilegnar.timer.views.viewholders;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class BaseViewHolder<T> extends LinearLayout {
    private static final int NOT_PRESENT_IN_LIST_VIEW = -1;

    private int position = NOT_PRESENT_IN_LIST_VIEW;
    private T item;

    public BaseViewHolder(Context activityContext) {
        this(activityContext, null);
    }

    public BaseViewHolder(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), getViewHolderResource(), this);
    }

    @LayoutRes
    protected abstract int getViewHolderResource();

    public abstract void loadData(T item);

    public int getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
