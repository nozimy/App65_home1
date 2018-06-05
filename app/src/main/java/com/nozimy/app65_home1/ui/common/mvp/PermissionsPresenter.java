package com.nozimy.app65_home1.ui.common.mvp;

public interface PermissionsPresenter{
    void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
