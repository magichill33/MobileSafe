package com.ly.nesw.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ly.nesw.MainActivity;
import com.ly.nesw.R;


public class RightMenuFragment extends Fragment implements OnItemClickListener {

	private View view;
	private String tag = "MenuFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i(tag, "onActivityCreated");
		ListView list_view = (ListView) view.findViewById(R.id.list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				initData());
		list_view.setAdapter(adapter);
		list_view.setOnItemClickListener(this);
	}

	private List<String> initData() {
		List<String> list = new ArrayList<String>();
		list.add("fragment1");
		list.add("fragment2");
		list.add("fragment3");
		list.add("fragment4");
		list.add("fragment5");
		return list;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i(tag, "onCreateView");
		view = LayoutInflater.from(getActivity()).inflate(R.layout.list_view,
				null);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Fragment f  = null;
		switch (position) {
		case 0:
			f  =new Fragment1();
			break;
			
		case 1:
			f  =new Fragment2();
			break;
		case 2:
			f  =new Fragment3();
			break;
		case 3:
			f  =new Fragment4();
			break;
		case 4:
			f  =new Fragment5();
			break;
		}
        switchFragment(f);
	}

	private void switchFragment(Fragment f) {
		// TODO Auto-generated method stub
		if(f != null){
			if(getActivity() instanceof MainActivity){
				((MainActivity)getActivity()).switchFragment(f);
			}
		}
	}

}
