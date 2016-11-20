package com.example.youdrawiguess.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youdrawiguess.R;
import com.example.youdrawiguess.adapter.CircleAdapter;

import java.util.ArrayList;
import java.util.List;

public class CircleFragment extends Fragment implements OnClickListener {

	private View messageLayout;

	private ViewPager excoo_viewPager;// 用来放置界面切换
	private PagerAdapter excoo_Adapter;// 初始化View适配器
	private Button btn_wardrobe, btn_collection;
	private List<View> excoo_view;// 用来存放衣酷模块界面
	private ListView lv_wardrobe;
	private ListView lv_collection;

	private CircleAdapter circleAdapter;

	private TextView tv_title;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		messageLayout = inflater.inflate(R.layout.tab_circle_layout, container,
				false);

		init();
		initView();

		return messageLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void init() {
		tv_title = (TextView) messageLayout.findViewById(R.id.tv_title);
		tv_title.setText("朋友圈");

		btn_wardrobe = (Button) messageLayout.findViewById(R.id.btn_wardrobe);
		btn_collection = (Button) messageLayout
				.findViewById(R.id.btn_collection);
		btn_wardrobe.setOnClickListener(this);
		btn_collection.setOnClickListener(this);
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("deprecation")
	private void initView() {
		excoo_viewPager = (ViewPager) messageLayout
				.findViewById(R.id.id_viewpage);
		excoo_viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			// ViewPage左右滑动时
			@Override
			public void onPageSelected(int arg0) {
				int currentItem = excoo_viewPager.getCurrentItem();
				switch (currentItem) {
				case 0:
					resetWardrobeColor();
					initWardrobe();
					break;
				case 1:
					resetCollectionColor();
					initCollection();
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View tab_wardrobe = layoutInflater.inflate(R.layout.tab_wardrobe, null);
		View tab_collection = layoutInflater.inflate(R.layout.tab_collection,
				null);

		excoo_view = new ArrayList<View>();
		excoo_view.add(tab_wardrobe);
		excoo_view.add(tab_collection);

		excoo_Adapter = new PagerAdapter() {
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(excoo_view.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = excoo_view.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return excoo_view.size();
			}

		};
		excoo_viewPager.setAdapter(excoo_Adapter);

		resetWardrobeColor();
		initWardrobe();
	}

	/**
	 * 初始化我的衣橱
	 * */
	private void initWardrobe() {
		lv_wardrobe = (ListView) excoo_view.get(0).findViewById(
				R.id.lv_wardrobe);
		lv_wardrobe.setVisibility(View.VISIBLE);
		circleAdapter = new CircleAdapter(getActivity());
		lv_wardrobe.setAdapter(circleAdapter);
	}

	/**
	 * 初始化我的收藏
	 * */
	private void initCollection() {
		lv_collection = (ListView) excoo_view.get(1).findViewById(
				R.id.lv_collection);
		lv_collection.setVisibility(View.VISIBLE);
	}

	/**
	 * 把衣橱tab页文字变暗
	 */
	private void resetWardrobeColor() {
		btn_wardrobe.setTextColor(getResources().getColor(R.color.black));
		btn_collection.setTextColor(getResources().getColor(R.color.red));
	}

	/**
	 * 把收藏tab页文字变暗
	 */
	private void resetCollectionColor() {
		btn_wardrobe.setTextColor(getResources().getColor(R.color.red));
		btn_collection.setTextColor(getResources().getColor(R.color.black));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_wardrobe:
			initWardrobe();
			excoo_viewPager.setCurrentItem(0);
			resetWardrobeColor();
			break;
		case R.id.btn_collection:
			initCollection();
			excoo_viewPager.setCurrentItem(1);
			resetCollectionColor();
			break;
		}
	}
}
