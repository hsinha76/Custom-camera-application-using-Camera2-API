package com.hsdroid.sdimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hsdroid.Constants;
import com.hsdroid.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.UUID;

public class ViewImage extends Activity {
	ImageButton upload;
	TextView text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_image);
		Intent i = getIntent();
		final int position = i.getExtras().getInt("position");
		final String[] d = i.getStringArrayExtra("filepath");
		final String[] n = i.getStringArrayExtra("filename");
		final ImageLoader imageLoader = new ImageLoader(getApplication());
		upload = findViewById(R.id.upload);
		ImageView imageView = findViewById(R.id.full_image_view);
		imageLoader.DisplayImage(d[position], imageView);
		
		text = findViewById(R.id.imagetext);
		text.setText(n[position]);

		upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uploadimage(d[position]);
			}
		});
	}

	public void uploadimage(String d)
	{
		try {
			String uploadId = UUID.randomUUID().toString();

			new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
					.addFileToUpload(d, "image") //Adding file
					.addParameter("name", text.getText().toString())
					.setNotificationConfig(new UploadNotificationConfig())
					.setMaxRetries(2)
					.startUpload();

		} catch (Exception exc) {
			Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
		}

	}
}