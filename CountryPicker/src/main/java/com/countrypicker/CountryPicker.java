package com.countrypicker;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountryPicker extends DialogFragment {

    public static final String TAG = "CountryPicker";

    /**
     * To support show as dialog
     *
     * @param dialogTitle String set as dialog title
     * @return CountryPicker instance
     */
    public static CountryPicker newInstance(String dialogTitle) {
        CountryPicker picker = new CountryPicker();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        return picker;
    }

    private EditText searchEditText;
    private ListView countryListView;
    private CountryListAdapter adapter;
    private CountryPickerListener listener;

    private CountryProvider countryProvider;

    private List<Country> selectedCountriesList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            countryProvider = new CountryProvider(getActivity());
            for (Country country : countryProvider.getCountries()) {
                selectedCountriesList.add(country);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_picker, container, false);

        // Set dialog title if show as dialog
        Bundle args = getArguments();
        if (args != null && getDialog() != null) {
            String dialogTitle = args.getString("dialogTitle");
            getDialog().setTitle(dialogTitle);

            int width = getResources().getDimensionPixelSize(
                    R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(
                    R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }

        // Get view components
        searchEditText = (EditText) view
                .findViewById(R.id.country_picker_search);
        countryListView = (ListView) view
                .findViewById(R.id.country_picker_listview);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new CountryListAdapter(getActivity(), selectedCountriesList);
        countryListView.setAdapter(adapter);
        countryListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null) {
                    Country country = selectedCountriesList.get(position);
                    listener.onSelectCountry(country.getName(),
                            country.getCode());
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    /**
     * Get the listener to receive countries set
     *
     * @return Country selected listener
     */
    public CountryPickerListener getListener() {
        return listener;
    }

    /**
     * Set the listener to receive the country selected
     *
     * @param listener Country selected listener
     */
    public void setListener(CountryPickerListener listener) {
        this.listener = listener;
    }

    /**
     * Get the edit text for filtering countries. Use this to customize the view
     *
     * @return Filter edit text
     */
    public EditText getSearchEditText() {
        return searchEditText;
    }

    /**
     * Get the list view showing the countries. Use this to customize the view
     *
     * @return List view containing countries
     */
    public ListView getCountryListView() {
        return countryListView;
    }

    private void filter(String text) {
        selectedCountriesList.clear();
        for (Country country : countryProvider.getCountries()) {
            if (country.getName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCountriesList.add(country);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
