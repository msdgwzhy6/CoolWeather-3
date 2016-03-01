package com.jadyn.coolweather.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jadyn.coolweather.R;
import com.jadyn.coolweather.model.City;
import com.jadyn.coolweather.model.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JadynAi on 2016/2/27.
 */
public class CityExpandAdapter extends BaseExpandableListAdapter{
    private List<City> cities;

    private ArrayList<List<Country>> countries;

    private Context context;

    public CityExpandAdapter(List<City> cities, 
                             ArrayList<List<Country>> countries, Context context) {
        this.cities = cities;
        this.countries = countries;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return cities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return countries.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return countries.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderCity holderCity;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_expand, parent, false);

            holderCity = new ViewHolderCity();
            holderCity.cityText = (TextView) convertView.findViewById(R.id.expand_item_city);
            convertView.setTag(holderCity);
        } else {
            holderCity = (ViewHolderCity) convertView.getTag();
        }
        holderCity.cityText.setText(cities.get(groupPosition).getCityName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderCountry holderItem;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_country_expand,parent,false);

            holderItem=new ViewHolderCountry();
            holderItem.countryText= (TextView) convertView.findViewById(R.id.expand_item_country);

            convertView.setTag(holderItem);
        }else{
            holderItem= (ViewHolderCountry) convertView.getTag();
        }

        holderItem.countryText.setText(countries.get(groupPosition).get(childPosition).countryName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolderCity {
        private TextView cityText;
    }

    class ViewHolderCountry {
        private TextView countryText;
    }
}
