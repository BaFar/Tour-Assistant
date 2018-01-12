package com.example.dell.tourassistant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by DELL on 1/8/2018.
 */

public class PermissionUtil {
    private static final String PERMISSION_FILE_NAME = "all_permission_status";
    public static final int INTERNET_REQUEST_CODE = 101;
    public static final int CAMERA_REQUEST_CODE= 102;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE= 103;
    public static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 104;
    public static final int APP_SETTINGS_REQUEST_CODE = 201;

     /*
        * Check if version is marshmallow and above.
        * Used in deciding to ask runtime permission
        * */
     private static final boolean shouldAskPermission(){
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
             return true;
         else return false;
     }

     private static final boolean shouldAskPermission(Context context, String permission){

         if (shouldAskPermission()){
             int permissionResult = ActivityCompat.checkSelfPermission(context,permission);
             if (permissionResult != PackageManager.PERMISSION_GRANTED){
                 return true;
             }
         }
         return false;
     }

     /*when asking first time */
     private static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime){
         SharedPreferences preferences = context.getSharedPreferences(PERMISSION_FILE_NAME,Context.MODE_PRIVATE);
         preferences.edit().putBoolean(permission,isFirstTime).apply();
     }

     private static  boolean isFirstTimeAskingPermission(Context context,String permission){
         return context.getSharedPreferences(PERMISSION_FILE_NAME,Context.MODE_PRIVATE).getBoolean(permission,true);
     }

     /*now check actual permission*/
     public static void checkPermission(Context context,String permission,PermissionAskListener listener){

         /*if permission is not granted*/
         if (shouldAskPermission(context,permission)){

             /*if permission denied previously*/
             if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,permission)){
                 listener.onPermissionPreviouslyDenied();
             }else {
                 /*it's first time to ask permission*/
                 if (isFirstTimeAskingPermission(context,permission)){
                     firstTimeAskingPermission(context,permission,false);/*set is first time false for later*/
                     listener.onNeedPermission();
                 }
                 else {
                     /*Not the permission allowed not to ask here, user disabled permission with "Don't Ask Again".
                      Handle the feature without permission or ask user to manually allow permission*/
                     listener.onPermissionDisabled();
                 }
             }
         }
         else {
             /*permission granted*/
             listener.onPermissionGranted();
         }

     }
     /*
        * Callback on various cases on checking permission
        *
        * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
        *     If permission is already granted, onPermissionGranted() would be called.
        *
        * 2.  Above M, if the permission is being asked first time onNeedPermission() would be called.
        *
        * 3.  Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
        *     would be called.
        *
        * 4.  Above M, if the permission is disabled by device policy or the user checked "Never ask again"
        *     check box on previous request permission, onPermissionDisabled() would be called.
        * */

     public interface PermissionAskListener{
         /*Callback to ask permission*/
          void onNeedPermission();

         /* callback on permission denied*/
          void onPermissionPreviouslyDenied();

         /*callback when permission previously denied with "Don't Ask Again*/
          void onPermissionDisabled();

         /*callback on permission granted*/
          void onPermissionGranted();

     }

}
