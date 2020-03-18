package com.hxypay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hxypay.R;
import com.hxypay.response.FellowBusinessRes;
import com.hxypay.response.FriendsRes;
import com.hxypay.util.VerifyRuler;

import java.util.List;

public class FragmentBusinessExpandableListAdapter extends BaseExpandableListAdapter {
    private List<FellowBusinessRes.Inif.Friend> friend;
    private List<FellowBusinessRes.Inif.MonthTotal> monthTotal;
    private String []groupitem_name={"每月交易数据","好友交易数据"};
    private Context context;
    private int progress;

    public FragmentBusinessExpandableListAdapter(Context context, List<FellowBusinessRes.Inif.Friend> friend, List<FellowBusinessRes.Inif.MonthTotal> monthTotal){
        this.context = context;
        this.friend = friend;
        this.monthTotal = monthTotal;
    }
    //        获取分组的个数
    @Override
    public int getGroupCount() {
        return groupitem_name.length;
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition==0){
            return monthTotal ==null ? 0:monthTotal.size();
        } else if (groupPosition ==1) {
            return friend==null ?0:friend.size();
        }
        return 0;
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupitem_name[groupPosition];
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0){
          return monthTotal == null ?0:monthTotal.size();
        }
        else if (groupPosition == 1){
            return friend==null ? 0:friend.size();
        }
        return null;
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //        获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Groupitem groupitem;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group,parent,false);
            groupitem = new Groupitem();
            groupitem.textView = convertView.findViewById(R.id.tv_item);
            convertView.setTag(groupitem);
        }else {
            groupitem = (FragmentBusinessExpandableListAdapter.Groupitem) convertView.getTag();
        }
        groupitem.textView.setText(groupitem_name[groupPosition]);

        return convertView;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        chilItem chilItem;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.friends_business_item,parent,false);
            chilItem = new chilItem();
            chilItem.tv_date_item = (TextView) convertView.findViewById(R.id.tv_date_item);
            chilItem.tv_money_item = (TextView) convertView.findViewById(R.id.tv_money_item);
            chilItem.pb_item =  convertView.findViewById(R.id.pb_item);
            convertView.setTag(chilItem);
        }else {
            chilItem = (FragmentBusinessExpandableListAdapter.chilItem) convertView.getTag();
        }
        LayerDrawable layerDrawable = (LayerDrawable) chilItem.pb_item.getProgressDrawable();
        ScaleDrawable scaleDrawable = (ScaleDrawable) layerDrawable.getDrawable(1);
        GradientDrawable gradientDrawable = (GradientDrawable) scaleDrawable.getDrawable();
        chilItem.pb_item.setMax(80000);
        if (groupPosition == 0){
            try {
                chilItem.tv_date_item.setText(monthTotal.get(childPosition).getDate());
                chilItem.tv_money_item.setText(monthTotal.get(childPosition).getMoney()+"(元)");
                progress = (int)Float.parseFloat(monthTotal.get(childPosition).getMoney().replace(",",""));
                if (android.os.Build.VERSION.SDK_INT > 23) {//android 7.0以上才有此方法；
                    chilItem.pb_item.setProgress(progress,true);
                }else {
                    chilItem.pb_item.setProgress(progress);
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }else if (groupPosition == 1){
            try {
                chilItem.tv_date_item.setText(VerifyRuler.nameShow(friend.get(childPosition).getName()));
                chilItem.tv_money_item.setText(friend.get(childPosition).getTotal()+"(元)");
                progress = (int)(Float.parseFloat(friend.get(childPosition).getTotal().replace(",","")));
                if (android.os.Build.VERSION.SDK_INT > 23) {//android 7.0以上才有此方法；
                    chilItem.pb_item.setProgress(progress,true);
                }else {
                    chilItem.pb_item.setProgress(progress);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (childPosition % 2 ==0){
            gradientDrawable.setColor(Color.parseColor("#47d9fe"));
        }else {
            gradientDrawable.setColor(Color.parseColor("#fdcb46"));
        }

        return convertView;
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class Groupitem{
        TextView textView;
    }
    class chilItem{
        TextView tv_date_item,tv_money_item;
        ProgressBar pb_item;
    }
}
