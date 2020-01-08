package com.hsdroid.sdimage;

import java.io.File;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.hsdroid.R;

@SuppressWarnings("ALL")
public class MainActivity extends Activity {

	private String[] FilePathStrings;
	private String[] FileNameStrings;
	private File[] listFile;
	GridView grid;
	LazyAdapter adapter;
	File file;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_main);

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
					.show();
		} else {
			file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "Pictures");
			file.mkdirs();
		}

		if (file.isDirectory()) {
			listFile = file.listFiles();
			FilePathStrings = new String[listFile.length];
			FileNameStrings = new String[listFile.length];

			for (int i = 0; i < listFile.length; i++) {
				FilePathStrings[i] = listFile[i].getAbsolutePath();
				FileNameStrings[i] = listFile[i].getName();
			}
		}

		grid = (GridView) findViewById(R.id.gridview);
		adapter = new LazyAdapter(this, FilePathStrings, FileNameStrings);
		grid.setAdapter(adapter);

		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getApplicationContext(), ViewImage.class);
				i.putExtra("filepath", FilePathStrings);
				i.putExtra("filename", FileNameStrings);
				i.putExtra("position", position);
				startActivity(i);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add("Clear Cache")
				.setOnMenuItemClickListener(this.ClearCacheButtonClickListener)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return super.onCreateOptionsMenu(menu);
	}

	OnMenuItemClickListener ClearCacheButtonClickListener = new OnMenuItemClickListener() {
		public boolean onMenuItemClick(MenuItem item) {

			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
			return false;

		}
	};
}
