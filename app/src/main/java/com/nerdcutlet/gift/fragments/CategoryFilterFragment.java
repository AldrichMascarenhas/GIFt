package com.nerdcutlet.gift.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nerdcutlet.gift.R;
import com.nerdcutlet.gift.models.Categories;
import com.nerdcutlet.gift.models.FavouriteGif;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Aldrich on 03-04-2016.
 */
public class CategoryFilterFragment extends DialogFragment {

    public final static String LOG_TAG = "CategoryFilterFragment";

    OnFilterSelectedListener mCallback;
    String category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category_filter, container, false);
        ButterKnife.bind(this, rootView);

        final ListView categoryListView = (ListView) rootView.findViewById(R.id.category_listview);
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)rootView.findViewById(R.id.gif_category_Add);

        Button button_category_Add = (Button)rootView.findViewById(R.id.button_category_Add);

        Button set_filter = (Button)rootView.findViewById(R.id.set_filter);
        set_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onFilterSelected(category);

                dismiss();
            }
        });



        button_category_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                category = autoCompleteTextView.getText().toString();
                Log.d(LOG_TAG, "category : " + category);

                Categories categories = new Categories(category);
                categories.save();



            }
        });

        List<Categories> categories = Categories.listAll(Categories.class);
        ArrayList<String> categoriesList = new ArrayList<>();

        if(!categories.isEmpty()){
            for(int i =0; i< categories.size(); i++){
                categoriesList.add(categories.get(i).getGifCategory());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoriesList);
                categoryListView.setAdapter(adapter);
            }
        }else{
            Toast.makeText(getActivity(), "No saved categories",Toast.LENGTH_LONG).show();
        }

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                category = categoryListView.getItemAtPosition(position).toString();

            }
        });

        getDialog().setTitle("Hi");
        return rootView;
    }



    public interface OnFilterSelectedListener {
        public void onFilterSelected(String category);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFilterSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnFilterSelectedListener");
        }

    }

}

