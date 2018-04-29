package com.countrypicker;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class CountryPicker extends DialogFragment implements
		Comparator<Country> {
	/**
	 * View components
	 */
	private EditText searchEditText;
	private ListView countryListView;

	/**
	 * Adapter for the listview
	 */
	private CountryListAdapter adapter;

	/**
	 * Hold all countries, sorted by country name
	 */
	private List<Country> allCountriesList;

	/**
	 * Hold countries that matched user query
	 */
	private List<Country> selectedCountriesList;

	/**
	 * Listener to which country user selected
	 */
	private CountryPickerListener listener;

	/**
	 * Use collator to sort locale-aware.
	 */
	private static final Collator sCollator = Collator.getInstance();


	/**
	 * Set listener
	 *
	 * @param listener
	 */
	public void setListener(CountryPickerListener listener) {
		this.listener = listener;
	}

	public EditText getSearchEditText() {
		return searchEditText;
	}

	public ListView getCountryListView() {
		return countryListView;
	}

	/**
	 * Convenient function to get currency code from country code currency code
	 * is in English locale
	 *
	 * @param countryCode
	 * @return
	 */
	public static Currency getCurrencyCode(String countryCode) {
		try {
			return Currency.getInstance(new Locale("en", countryCode));
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * Get all countries with code and name from res/raw/countries.json
	 *
	 * @return
	 */
	private List<Country> getAllCountries() {
		if (allCountriesList == null) {
			try {
				allCountriesList = new ArrayList<Country>();

				for (String cc : Locale.getISOCountries()) {
					Country country = new Country();
					country.setCode(cc);
					country.setName(new Locale("", cc).getDisplayCountry());
					allCountriesList.add(country);
				}

				// Sort the all countries list based on country name
				Collections.sort(allCountriesList, this);

				// Initialize selected countries with all countries
				selectedCountriesList = new ArrayList<Country>();
				selectedCountriesList.addAll(allCountriesList);

				// Return
				return allCountriesList;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * To support show as dialog
	 *
	 * @param dialogTitle
	 * @return
	 */
	public static CountryPicker newInstance(String dialogTitle) {
		CountryPicker picker = new CountryPicker();
		Bundle bundle = new Bundle();
		bundle.putString("dialogTitle", dialogTitle);
		picker.setArguments(bundle);
		return picker;
	}

	/**
	 * Create view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate view
		View view = inflater.inflate(R.layout.country_picker, container, false);

		// Get countries from the json
		getAllCountries();

		// Set dialog title if show as dialog
		Bundle args = getArguments();
		if (args != null) {
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

		// Set adapter
		adapter = new CountryListAdapter(getActivity(), selectedCountriesList);
		countryListView.setAdapter(adapter);

		// Inform listener
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

		// Search for which countries matched user query
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				search(s.toString());
			}
		});

		return view;
	}

	/**
	 * Search allCountriesList contains text and put result into
	 * selectedCountriesList
	 *
	 * @param text
	 */
	@SuppressLint("DefaultLocale")
	private void search(String text) {
		selectedCountriesList.clear();

		for (Country country : allCountriesList) {
			if (country.getName().toLowerCase(Locale.ENGLISH)
					.contains(text.toLowerCase())) {
				selectedCountriesList.add(country);
			}
		}

		adapter.notifyDataSetChanged();
	}

	/**
	 * Support sorting the countries list
	 */
	@Override
	public int compare(Country lhs, Country rhs) {
		return sCollator.compare(lhs.getName(), rhs.getName());
	}

}
