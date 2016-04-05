package com.github.learn.expandable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.github.captain_miao.recyclerviewutils.listener.OnRecyclerItemClickListener;
import com.github.learn.refreshandload.R;

import java.util.List;


public class SimpleExpandableAdapter extends ExpandableRecyclerAdapter<SimpleParentViewHolder, SimpleChildViewHolder> implements OnRecyclerItemClickListener {

    private LayoutInflater mInflater;

    public SimpleExpandableAdapter(Context context, List<ParentListItem> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SimpleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_view_parent, viewGroup, false);
        return new SimpleParentViewHolder(view);
    }

    @Override
    public SimpleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_view_child, viewGroup, false);
        SimpleChildViewHolder viewHolder =  new SimpleChildViewHolder(view);
        viewHolder.setOnRecyclerItemClickListener(this);
        viewHolder.addOnItemViewClickListener();
        viewHolder.addOnViewClickListener(viewHolder.mCheckBox);
        return viewHolder;
    }

    @Override
    public void onBindParentViewHolder(SimpleParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        SimpleParentItem simpleParentItem = (SimpleParentItem) parentListItem;
        parentViewHolder.mTvTitle.setText(simpleParentItem.getTitle());
    }

    @Override
    public void onBindChildViewHolder(SimpleChildViewHolder simpleChildViewHolder, int position, Object childListItem) {
        SimpleChild simpleChild = (SimpleChild) childListItem;
        simpleChildViewHolder.mTvContent.setText(simpleChild.getContent());
        simpleChildViewHolder.mCheckBox.setChecked(simpleChild.isSolved());
    }

    @Override
    public void onClick(View v, int position) {
        int id = v.getId();
        Object item = getItem(position);
        switch (id){
            case R.id.child_list_item_check_box:
                if (item instanceof SimpleChild) {
                    CheckBox checkBox = (CheckBox) v;
                    ((SimpleChild) item).setSolved(checkBox.isChecked());
                    notifyItemChanged(position);
                    Toast.makeText(v.getContext(), "Click check_box", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                    int itemPosition = (int) getItemId(position);
                    int parentPosition = getParentPosition(itemPosition);
                    int parentIndex = getParentWrapperIndex(parentPosition);
                    if (item instanceof SimpleChild) {
                        Toast.makeText(v.getContext(), "Click ParentIndex = " + parentPosition
                                + "ChildIndex = " + (itemPosition - parentIndex - 1)
                                + " Child = " + ((SimpleChild) item).getContent(), Toast.LENGTH_LONG).show();
                    }
                break;
        }
    }
}
