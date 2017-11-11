package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.List;

import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.views.viewholders.BaseViewHolder;

/**
 * A base arrayAdapter which re-uses views (convertView) and sets basic data on the {@link BaseViewHolder}, like the
 * position and item) T is the type of item used in the adapter and V is the View of the type BaseViewHolder, which is
 * supplemented with the item's data. V extends LinearLayout instead of View so the margins can be changed using the
 * {@link BaseArrayAdapterHelper}.
 */
public abstract class BaseArrayAdapter<T, V extends BaseViewHolder<T>> extends ArrayAdapter<T> {
    /** Layout resource is inflated in ViewHolder itself, so no resource is given to the constructor */
    private static final int NO_LAYOUT_RESOURCE_ID = 0;

    private final BaseArrayAdapterHelper baseArrayAdapterHelper = new BaseArrayAdapterHelper();

    protected final T ITEM_DOES_NOT_EXIST = null;

    private SparseArray<V> allViewHolders = new SparseArray<>();

    protected BaseArrayAdapter(Context context, List<T> items) {
        super(context, NO_LAYOUT_RESOURCE_ID, items);
    }

    @NonNull
    @Override
    public V getView(int position, View convertView, @NonNull ViewGroup viewGroup) {
        // noinspection unchecked ... Don't call super: layout inflation will be executed by the ViewHolder
        V viewHolder = getReUsableView((V) convertView);

        if (shouldAddTopMarginForFirstItem()) {
            viewHolder = baseArrayAdapterHelper.setTopMarginForFirstItem(viewHolder, position);
        }

        viewHolder = setViewHolderData(position, viewHolder);
        allViewHolders.put(position, viewHolder);
        return viewHolder;
    }

    private V getReUsableView(V convertView) {
        if (convertView == null) {
            return getNewView(getContext());
        } else {
            return convertView;
        }
    }

    protected abstract V getNewView(Context activityContext);

    /**
     * Als je een top-margin / padding op een listview zet zullen views die onder die padding/margin gescrolled worden
     * daar niet zichtbaar zijn. Om dit effect te voorkomen moet deze top-margin / -padding op de listitems zelf
     * gedefinieerd worden. Om deze padding/margin los te kunnen gebruiken van de default paddings/margin in een
     * listview wordt ALLEEN bij het eerste item een (extra) topmargin toegevoegd. ViewHolders kunnen vervolgens
     */
    private boolean shouldAddTopMarginForFirstItem() {
        return getTopMarginDimenResForFirstItem() != 0;
    }

    protected V setViewHolderData(int position, V viewHolder) {
        T item = getItem(position);
        viewHolder.setPosition(position);
        viewHolder.setItem(item);
        viewHolder.loadData(item);
        return viewHolder;
    }

    public SparseArray<V> getAllViewHolders() {
        return allViewHolders;
    }

    public V getViewHolder(int position) {
        return getAllViewHolders().get(position);
    }

    protected boolean isFirstItem(int position) {
        return position == 0;
    }

    protected T getPreviousItem(int position) {
        boolean isFirstItem = position == 0;
        return isFirstItem ? ITEM_DOES_NOT_EXIST : getItem(position - 1);
    }

    protected T getNextItem(int position) {
        boolean isLastItem = position == getCount() - 1;
        return isLastItem ? ITEM_DOES_NOT_EXIST : getItem(position + 1);
    }

    private class BaseArrayAdapterHelper {
        private Integer originalTopMargin;

        /**
         * The implementation can be improved to support margins for views other than LinearLayout. This should make the
         * complete class more generic!
         */
        V setTopMarginForFirstItem(V viewHolder, int position) {
            View rootView = viewHolder.getChildAt(0);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rootView.getLayoutParams();
            int topMargin = getOriginalTopMargin(params);
            topMargin = addExtraMarginToFirstItem(position, topMargin);
            params.setMargins(params.leftMargin, topMargin, params.rightMargin, params.bottomMargin);
            rootView.setLayoutParams(params);
            return viewHolder;
        }

        private int addExtraMarginToFirstItem(int position, int newMargin) {
            if (isFirstItem(position)) {
                newMargin += Res.getDimension(getTopMarginDimenResForFirstItem());
            }
            return newMargin;
        }

        private int getOriginalTopMargin(LinearLayout.LayoutParams params) {
            if (originalTopMargin == null) {
                originalTopMargin = params.topMargin;
            }
            return originalTopMargin;
        }
    }

    @DimenRes
    protected int getTopMarginDimenResForFirstItem() {
        return 0;
    }
}
