package com.nozimy.app65_home1.ui.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nozimy.app65_home1.app.ContactListApp;
import com.nozimy.app65_home1.di.contact.ContactComponent;
import com.nozimy.app65_home1.ui.contact.mvp.ContactDetailsContract;
import com.nozimy.app65_home1.ui.contacts.ContactListFragment;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.ui.map.MyMapFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContactFragment extends Fragment implements ContactDetailsContract.View{

    @Inject
    ContactDetailsContract.Presenter contactPresenter;

    @BindView(R.id.details_text) TextView nameTextView;
    @BindView(R.id.email) TextView emailTextView;
    @BindView(R.id.phone_number) TextView phoneTextView;
    @BindView(R.id.contactDetailsProgressBarWrap) LinearLayout contactDetailsProgressBarWrap;
    @BindView(R.id.set_address_btn)
    Button addressBtn;
    Unbinder unbinder;

    @OnClick(R.id.set_address_btn)
    void setAddressBtn(){
        // todo: Неявные зависимости
        MyMapFragment myMapFragment = MyMapFragment.newInstance( getIdFromArguments(), false);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, myMapFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    public ContactFragment() {

    }

    public static ContactFragment newInstance(String mContactId) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ContactListFragment.DETAILS_KEY, mContactId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        ContactListApp contactListApp = ((ContactListApp) getActivity().getApplication());
        ContactComponent contactComponent = contactListApp.getAppComponent().plusContactComponent();
        contactComponent.inject(this);

        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contactPresenter.onAttach(this);
        contactPresenter.getContact(getIdFromArguments());
    }

    public String getIdFromArguments() {
        return getArguments().getString(ContactListFragment.DETAILS_KEY);
    }

    @Override
    public void onStop() {
        super.onStop();

        contactPresenter.clearDisposable();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showContact(String name, String phones, String emails) {
            nameTextView.setText(name);
            phoneTextView.setText(phones);
            emailTextView.setText(emails);

            setProgressBarGone();
    }

    @Override
    public void setProgressBarVisible() {
        contactDetailsProgressBarWrap.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressBarGone() {
        contactDetailsProgressBarWrap.setVisibility(View.GONE);
    }
}