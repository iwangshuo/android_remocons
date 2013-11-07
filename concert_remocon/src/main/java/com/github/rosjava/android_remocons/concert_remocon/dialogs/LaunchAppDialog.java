package com.github.rosjava.android_remocons.concert_remocon.dialogs;

/**
 * Created by jorge on 11/7/13.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.github.rosjava.android_remocons.concert_remocon.R;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Custom alert dialog to prompt launching an app
 */
public class LaunchAppDialog extends AlertDialogWrapper {
    public LaunchAppDialog(Activity context) {
        super(context, new AlertDialog.Builder(context), "Launch", "Cancel");
    }

    public void setup(concert_msgs.RemoconApp app, boolean allowed, String reason) {

        boolean hasValidIcon = false;
        String iconFormat = app.getIcon().getFormat();
        ChannelBuffer buffer = app.getIcon().getData();

        if (buffer.array().length > 0 && iconFormat != null &&
           (iconFormat.equals("jpeg") || iconFormat.equals("png"))) {
            Bitmap iconBitmap =
                    BitmapFactory.decodeByteArray(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
            if (iconBitmap != null) {
                dialog.setIcon(new BitmapDrawable(context.getResources(), iconBitmap));
                hasValidIcon = true;
            }
        }

        if (! hasValidIcon) {
            dialog.setIcon(context.getResources().getDrawable(R.drawable.question_mark));
        }

        dialog.setTitle(app.getDisplayName());

        if (allowed) {
            Log.i("ConcertRemocon", "Concert allowed use selected app. " + reason);
            dialog.setMessage(app.getDescription() + "\n\nConcert allows you to start this app!");
        }
        else {
            Log.i("ConcertRemocon", "Concert denies the use of selected app. " + reason);
            dialog.setMessage(app.getDescription() + "\n\nConcert doesn't allow you to start this app\n" + reason);
            enablePositive = false;
        }
    }
}
