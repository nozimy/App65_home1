package com.nozimy.app65_home1.ui.route;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.SupportMapFragment;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.app.ContactListApp;
import com.nozimy.app65_home1.di.route.RouteComponent;
import com.nozimy.app65_home1.ui.route.mvp.RouteContract;
import com.nozimy.app65_home1.utils.CommonUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment implements RouteContract.View{

    @Inject
    RouteContract.Presenter presenter;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public static final int POINT_A = 0;
    public static final int POINT_B = 1;

    OnInteractionListener interactionListener;

    Unbinder unbinder;
    @BindView(R.id.a_point)
    Button buttonA;
    @BindView(R.id.b_point)
    Button buttonB;

    @OnClick(R.id.a_point)
    void setButtonA(){
        interactionListener.openContactChooseScreen(POINT_A);
    }
    @OnClick(R.id.b_point)
    void setButtonB(){
        interactionListener.openContactChooseScreen(POINT_B);
    }

    public RouteFragment() {
    }


    public static RouteFragment newInstance(String param1) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        ContactListApp contactListApp = ((ContactListApp) getActivity().getApplication());
        RouteComponent routeComponent = contactListApp.getAppComponent().plusRouteComponent();
        routeComponent.inject(this);

        presenter.onAttach(this);
        super.onAttach(context);

        super.onAttach(context);
        if (context instanceof OnInteractionListener) {
            interactionListener = (OnInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        presenter.init();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.route_map);
        mapFragment.getMapAsync(presenter);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showToast(String message) {
        CommonUtils.showToast(getContext(), message);
    }

    public interface OnInteractionListener{
        void openContactChooseScreen(int pointType);
        void popBackStack();
    }

    public void setPoint(int pointType, String contactId){

        presenter.loadContact(contactId, pointType);

//        if(pointType == POINT_A){
//            contactAId = contactId;
//
//            CommonUtils.showToast(getContext(), "A "+contactId);
//        } else if (pointType == POINT_B){
//            contactBId = contactId;
//
//            CommonUtils.showToast(getContext(), "B "+contactId);
//        }
        interactionListener.popBackStack();
    }

    @Override
    public void setButtonAText(String a){
        buttonA.setText(a);
    }

    @Override
    public void setButtonBText(String b){
        buttonB.setText(b);
    }
}

