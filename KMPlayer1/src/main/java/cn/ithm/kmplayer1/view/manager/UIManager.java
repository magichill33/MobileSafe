package cn.ithm.kmplayer1.view.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import cn.ithm.kmplayer1.GloableParameters;
import cn.ithm.kmplayer1.R;

public class UIManager {
	private static UIManager instance = new UIManager();

	public static UIManager getInstance() {
		return instance;
	}

	private UIManager() {
	}

	public void changeFragment(Fragment target, Bundle bundle) {
		if (target == null)
			return;
		if (bundle != null)
			target.setArguments(bundle);

		FragmentManager manager = GloableParameters.MAIN.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.replace(R.id.ii_middle, target);
//		transaction.addToBackStack(null);
		transaction.commit();
	}
}
