package com.nikvay.business_application.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.nikvay.business_application.R;
import com.nikvay.business_application.activity.SearchActivity;
import com.nikvay.business_application.adapter.ProductPriceRecyclerAdapter;
import com.nikvay.business_application.common.ServerConstants;
import com.nikvay.business_application.model.CNP;
import com.nikvay.business_application.model.Product;
import com.nikvay.business_application.utils.Logs;
import com.nikvay.business_application.utils.ResponseUtil;
import com.nikvay.business_application.utils.SharedUtil;
import com.nikvay.business_application.volley_support.MyVolleyPostFragmentMethod;
import com.nikvay.business_application.volley_support.VolleyCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tamboli on 18-Mar-18.
 */

public class PriceListFragment extends Fragment {

    private static final String TAG = PriceListFragment.class.getSimpleName();
    TextView txt_no_data_found;
    RecyclerView recyclerView;
    ArrayList<Product> products;
    ProductPriceRecyclerAdapter adapter;
    SharedUtil sharedUtil;

    // Index from which pagination should start (0 is 1st page in our case)
    private static final int PAGE_START = 0;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // total no. of pages to load. Initial load is page 0, after which 2 more pages will load.
    private int TOTAL_PAGES = 3;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_price_list, container, false);
        initView(view);
        callWebServices();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("type", "price");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
        sharedUtil = new SharedUtil(getActivity());
        products = new ArrayList<>();
        txt_no_data_found = (TextView) view.findViewById(R.id.txt_no_data_found);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearlayout = new LinearLayoutManager(getActivity());
        linearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearlayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
    }

    private void callWebServices() {
        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(String response, int serviceCode) {
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.PRICE_LIST:
                        try {
                          CNP cnp = ResponseUtil.getPriceList(response);
                             if (cnp.getError_code() == 1) {

                                products.addAll(cnp.getProducts());

                                if (products.size() != 0) {
                                    txt_no_data_found.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    Logs.showLogE(TAG, response);


                                    adapter = new ProductPriceRecyclerAdapter(getActivity(), products);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();


                                } else {
                                    txt_no_data_found.setVisibility(View.VISIBLE);
                                    txt_no_data_found.setText("No data found");
                                    recyclerView.setVisibility(View.GONE);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onTaskFailed(String response, int serviceCode) {
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.PRICE_LIST:
                        Logs.showLogE(TAG, response);
                        break;
                }
            }
        };

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.PRICE_LIST);
        new MyVolleyPostFragmentMethod(getActivity(), volleyCompleteListener, map, ServerConstants.ServiceCode.PRICE_LIST, true);
    }

}
