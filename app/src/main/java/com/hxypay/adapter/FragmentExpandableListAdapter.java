package com.hxypay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxypay.R;
import com.hxypay.response.FriendsRes;
import com.hxypay.util.VerifyRuler;

import java.util.List;

public class FragmentExpandableListAdapter extends BaseExpandableListAdapter {
    private List<FriendsRes.Info1> direct;
    private List<FriendsRes.Info1> indirect;
    private String []groupitem_name={"直接","间接"};
    private Context context;
    public FragmentExpandableListAdapter(Context context, List<FriendsRes.Info1> direct,List<FriendsRes.Info1> indirect){
        this.context = context;
        this.direct = direct;
        this.indirect = indirect;
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
            return direct==null ?0:direct.size();
        } else if (groupPosition ==1) {
            return indirect ==null ? 0:indirect.size();
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
            return direct==null ? 0:direct.size();
        }
        else if (groupPosition == 1){
            return indirect == null ?0:indirect.size();
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
            groupitem = (FragmentExpandableListAdapter.Groupitem) convertView.getTag();
        }
        groupitem.textView.setText(groupitem_name[groupPosition]);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        chilItem chilItem;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_friends_item,parent,false);
            chilItem = new chilItem();
            chilItem.itemTele = (TextView) convertView.findViewById(R.id.tele_tx);
            chilItem.itemTime = (TextView) convertView.findViewById(R.id.time_tx);
            chilItem.tv_name_yes = (TextView) convertView.findViewById(R.id.tv_name_yes);
            convertView.setTag(chilItem);
        }else {
            chilItem = (FragmentExpandableListAdapter.chilItem) convertView.getTag();
        }
        if (groupPosition == 0){
            chilItem.itemTele.setText(VerifyRuler.teleShow(direct.get(childPosition).getPhoneNum().trim()));
            chilItem.itemTime.setText(VerifyRuler.timeShow(direct.get(childPosition).getCreated().trim()));
            if (TextUtils.isEmpty(direct.get(childPosition).getLoginId())||direct.get(childPosition).getLoginId().equals("1")) {
                chilItem.tv_name_yes.setText("未实名");
                chilItem.tv_name_yes.setTextColor(context.getResources().getColor(R.color.text_e72b2a_color));
            }else {
                chilItem.tv_name_yes.setText(VerifyRuler.nameShow(direct.get(childPosition).getLoginId().trim()));
                chilItem.tv_name_yes.setTextColor(context.getResources().getColor(R.color.text_333333_color));

            }
        }else if (groupPosition == 1){
            chilItem.itemTele.setText(VerifyRuler.teleShow(indirect.get(childPosition).getPhoneNum().trim()));
            chilItem.itemTime.setText(VerifyRuler.timeShow(indirect.get(childPosition).getCreated().trim()));
            if (TextUtils.isEmpty(indirect.get(childPosition).getLoginId())||indirect.get(childPosition).getLoginId().equals("1")) {
                chilItem.tv_name_yes.setText("未实名");
                chilItem.tv_name_yes.setTextColor(context.getResources().getColor(R.color.text_e72b2a_color));
            }else {
                chilItem.tv_name_yes.setText(VerifyRuler.nameShow(indirect.get(childPosition).getLoginId().trim()));
                chilItem.tv_name_yes.setTextColor(context.getResources().getColor(R.color.text_333333_color));

            }
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
        TextView itemTele,itemTime,tv_name_yes;
    }
}
