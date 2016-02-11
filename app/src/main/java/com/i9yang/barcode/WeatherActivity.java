package com.i9yang.barcode;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.i9yang.barcode.weather.Weather;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import java.text.SimpleDateFormat;

public class WeatherActivity extends Activity {
	private RealmResults<Weather> result;
	private SwipeMenuListView listView;
	private AppAdapter appAdapter;
	private Realm realm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_activity);

		listView = (SwipeMenuListView) findViewById(R.id.listView);
		realm = Realm.getInstance(this);
		RealmQuery<Weather> query = realm.where(Weather.class);
		result = query.findAll();
		result.sort("seq", Sort.DESCENDING);

		appAdapter = new AppAdapter();
		listView.setAdapter(appAdapter);

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				deleteItem.setIcon(R.drawable.ic_delete);
				deleteItem.setWidth(dp2px(90));
				menu.addMenuItem(deleteItem);
			}
		};

		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						realm.beginTransaction();
						result.remove(position);
						realm.commitTransaction();
						appAdapter.notifyDataSetChanged();
						break;
				}
				return false;
			}
		});
	}

	class AppAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return result == null ? 0 : result.size();
		}

		@Override
		public Weather getItem(int position) {
			return result.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			Weather item = getItem(position);

			SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
			String strDate = dateFormat.format(item.getDate());

			holder.tv_name.setText(strDate + " : " + item.getText() + " ( " + item.getTemperature() + " ) ");
			return convertView;
		}

		class ViewHolder {
			TextView tv_name;

			public ViewHolder(View view) {
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

	 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		Log.d("Test", "!!!!!!!!!!!!!test!!!!!!!!!!!");
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

