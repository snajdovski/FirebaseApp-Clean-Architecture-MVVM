package ro.alexmamo.firebaseapp.main.products;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import ro.alexmamo.firebaseapp.R;
import ro.alexmamo.firebaseapp.databinding.FragmentProductsBinding;

import static ro.alexmamo.firebaseapp.utils.HelperClass.getProductNameFirstLetterCapital;

public class ProductsFragment extends DaggerFragment implements Observer<PagedList<Product>>, ProductsAdapter.OnProductClickListener {
    @Inject ProductsViewModel viewModel;
    private FragmentProductsBinding fragmentProductsBinding;
    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        fragmentProductsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, null, false);
        View rootView = fragmentProductsBinding.getRoot();
        setHasOptionsMenu(true);
        initProductsRecyclerView();
        initProductsAdapter();
        loadProducts();
        return rootView;
    }

    private void initProductsRecyclerView() {
        productsRecyclerView = fragmentProductsBinding.productsRecyclerView;
    }

    private void initProductsAdapter() {
        productsAdapter = new ProductsAdapter(this);
        productsRecyclerView.setAdapter(productsAdapter);
    }

    private void loadProducts() {
        viewModel.pagedListLiveData.observe(getViewLifecycleOwner(), this);
    }

    private void reloadProducts() {
        viewModel.replaceSubscription(this, null);
        loadProducts();
    }

    private void loadSearchedProducts(String searchText) {
        viewModel.replaceSubscription(this, searchText);
        loadProducts();
    }

    @Override
    public void onChanged(PagedList<Product> products) {
        productsAdapter.submitList(products);
        hideProgressBar();
    }

    private void hideProgressBar() {
        fragmentProductsBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onProductClick(Product clickedProduct) {
        displayProductName(clickedProduct.name);
    }

    private void displayProductName(String name) {
        String productNameFirstLetterCapital = getProductNameFirstLetterCapital(name);
        Toast.makeText(getActivity(), productNameFirstLetterCapital, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_products, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        initSearchView(searchItem);
        initSearchViewEditText();
    }

    private void initSearchView(MenuItem searchItem) {
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setOnCloseListener(() -> {
            reloadProducts();
            return false;
        });
    }

    private void initSearchViewEditText() {
        EditText searchViewEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchViewEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                if (searchText.length() > 0) {
                    loadSearchedProducts(searchText.toLowerCase());
                }

                return false;
            }
        });
    }
}