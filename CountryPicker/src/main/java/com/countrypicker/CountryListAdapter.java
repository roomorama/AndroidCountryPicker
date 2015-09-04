package com.countrypicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class CountryListAdapter extends BaseAdapter {

    private Context context;
    private List<Country> countries;

    protected CountryListAdapter(Context context, List<Country> countries) {
        super();
        this.context = context;
        this.countries = countries;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Country getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO There is no id?
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cell cell;

        if (convertView != null && convertView.getTag() instanceof Cell) {
            cell = (Cell) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.country_picker_row, parent, false);
            cell = new Cell(convertView);
            convertView.setTag(cell);
        }

        Country country = getItem(position);
        cell.textView.setText(country.getName());
        cell.imageView.setImageResource(getResId(country));

        return convertView;
    }

    private int getResId(Country country) {
        String drawableName = "flag_" + country.getCode().toLowerCase(Locale.ENGLISH);
        return context.getResources()
                .getIdentifier(drawableName, "drawable", context.getPackageName());
    }

    /**
     * Holder for the cell
     */
    protected static class Cell {

        public TextView textView;
        public ImageView imageView;

        public Cell(View view) {
            textView = (TextView) view.findViewById(R.id.country_picker_row_title);
            imageView = (ImageView) view.findViewById(R.id.country_picker_row_icon);
        }
    }
}
