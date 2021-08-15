package com.mayur.document_vault;

import android.net.Uri;

public interface UploadToStorageInterface {
    public void onStart();
    public void onProgress(int progress);
    public void onSuccess(Uri downloadUri);
    public void onFailure();
}
