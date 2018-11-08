package com.didacsoftware.mybooks;

import android.support.v7.util.DiffUtil;

import com.didacsoftware.mybooks.Model.model;

import java.util.List;

public class ListItemDiffCallBack extends DiffUtil.Callback {
    private List<model> oldList, newList;

    public ListItemDiffCallBack(List<model> oldList, List<model> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
