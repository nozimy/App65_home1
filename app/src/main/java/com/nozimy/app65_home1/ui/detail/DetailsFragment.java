package com.nozimy.app65_home1.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nozimy.app65_home1.ContactsListApp;
import com.nozimy.app65_home1.DataRepository;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;
import com.nozimy.app65_home1.ui.detail.mvp.ContactDetailsContract;
import com.nozimy.app65_home1.ui.detail.mvp.ContactDetailsPresenter;
import com.nozimy.app65_home1.ui.listing.ContactsListFragment;
import com.nozimy.app65_home1.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class DetailsFragment extends Fragment implements ContactDetailsContract.View{

    ContactDetailsPresenter contactDetailsPresenter;

    @BindView(R.id.details_text) TextView nameTextView;
    @BindView(R.id.email) TextView emailTextView;
    @BindView(R.id.phone_number) TextView phoneTextView;
    @BindView(R.id.contactDetailsProgressBarWrap) LinearLayout contactDetailsProgressBarWrap;
    Unbinder unbinder;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(String mContactId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ContactsListFragment.DETAILS_KEY, mContactId);
        fragment.setArguments(args);
        return fragment;
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

        setupPresenter();
        contactDetailsPresenter.loadDetails();
    }

    public String getShownId() {
        return getArguments().getString(ContactsListFragment.DETAILS_KEY);
    }


    private void setupPresenter() {
        DataRepository repository = ((ContactsListApp) getActivity().getApplication()).getRepository();
        contactDetailsPresenter = new ContactDetailsPresenter(repository, getArguments().getString(ContactsListFragment.DETAILS_KEY));
        contactDetailsPresenter.onAttach(this);
    }

    @Override
    public void subscribeUi() {
        mDisposable.add(Flowable.zip(contactDetailsPresenter.getContact(),
                contactDetailsPresenter.getPhones(),
                contactDetailsPresenter.getEmails(),
                Flowable.timer(1000, TimeUnit.MILLISECONDS),
                (contact,phones,emails, timerValue)-> new Object[]{contact,phones,emails})
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__->contactDetailsProgressBarWrap.setVisibility(View.VISIBLE))
                .subscribe(data -> {
                    if(data != null){
                        Object[] arr = (Object[])data;
                        ContactEntity contact = (ContactEntity) arr[0];
                        List<PhoneEntity> phoneEntities = (List<PhoneEntity>) arr[1];
                        List<EmailEntity> emailEntities = (List<EmailEntity>) arr[2];

                        nameTextView.setText(contact.getDisplayName());
                        Log.d("onSubscribe", contact.getDisplayName());

                        StringBuilder sb = new StringBuilder();
                        for (PhoneEntity phone : phoneEntities) {
                            sb.append(String.format("%s\n",
                                    phone.getNumber()));
                        }
                        phoneTextView.setText(sb);

                        sb.setLength(0);
                        for (EmailEntity email : emailEntities) {
                            sb.append(String.format("%s\n",
                                    email.getAddress()));
                        }
                        emailTextView.setText(sb);

                        contactDetailsProgressBarWrap.setVisibility(View.GONE);
                    }
                }));
    }

    @Override
    public void onStop() {
        super.onStop();

        mDisposable.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}