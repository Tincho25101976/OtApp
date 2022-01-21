package com.vsg.helper.ui.widget.cropImage.callback;

import android.net.Uri;

public interface SaveCallback extends Callback {
  void onSuccess(Uri uri);
}
