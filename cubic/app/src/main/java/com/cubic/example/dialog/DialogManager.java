package com.cubic.example.dialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cubic.sdk.common.Logger;
import com.cubic.example.dialog.model.BaseDialogFragment;

import java.util.List;

public class DialogManager<D extends BaseDialogFragment> implements IDialogManager<D> {

    private FragmentManager mFragmentManager;

    public DialogManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public void showDialog(Class<D> clazz) {
        this.showDialog(clazz, null);
    }

    @Override
    public void showDialog(Class<D> clazz, Bundle bundle) {
        String tag = clazz.getSimpleName();
        Fragment f = mFragmentManager.findFragmentByTag(tag);
        if (f instanceof BaseDialogFragment) {
            ((BaseDialogFragment) f).dismiss();
        }

        D dialogFragment = getInstanceDialogFragment(clazz);
        if (bundle != null) {
            assert dialogFragment != null;
            dialogFragment.setArguments(bundle);
        }

        assert dialogFragment != null;
        try {
            dialogFragment.showNow(mFragmentManager, tag);
        } catch (Exception e) {
            Logger.exception(e);
        }
    }

    @Override
    public void hideDialog(Class<D> clazz) {
        String tag = clazz.getSimpleName();
        Fragment f = mFragmentManager.findFragmentByTag(tag);
        if (f instanceof BaseDialogFragment) {
            ((BaseDialogFragment) f).dismiss();
        }
    }

    @Override
    public void onDestroy() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment f : fragments) {
                if (f instanceof BaseDialogFragment)
                    ((BaseDialogFragment) f).dismiss();
            }
        }
    }

    private D getInstanceDialogFragment(Class<D> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            Logger.exception(e);
        } catch (IllegalAccessException e) {
            Logger.exception(e);
        }

        return null;
    }
}
