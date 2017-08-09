package com.goldemperor.StockCheck.WaitView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.goldemperor.MainActivity.OSSHelper;
import com.goldemperor.MainActivity.People;
import com.goldemperor.R;
import com.goldemperor.MainActivity.TakePhotoHelper;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.model.TResult;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mylhyl.superdialog.SuperDialog;
import com.mylhyl.superdialog.res.values.ColorRes;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nova on 2017/7/19.
 */

public class UpdataView extends TakePhotoFragment {

    private FragmentActivity act;

    private SharedPreferences dataPref;
    private SharedPreferences.Editor dataEditor;

    private EditText edit_auditor;
    private EditText edit_info;

    private BootstrapButton submit;

    private ImageView image1Btn;

    private ImageView image2Btn;

    private ImageView image1;

    private ImageView image2;

    private int imageIndex;
    private String image1Url = null;
    private String image2Url = null;
    private String info;
    //图片网络加载设置
    ImageOptions imageOptions;

    private TakePhotoHelper PhotoHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_updata, null);
        super.onCreate(savedInstanceState);
        act = getActivity();
        //设置图片加载属性
        imageOptions = new ImageOptions.Builder()
                .setLoadingDrawableId(R.drawable.loading)
                .setFailureDrawableId(R.drawable.loading_failure)
                .setUseMemCache(true)
                .build();

        dataPref = act.getSharedPreferences(define.SharedName, 0);
        dataEditor = dataPref.edit();

        edit_info = (EditText) view.findViewById(R.id.edit_info);
        edit_info.setText(dataPref.getString(define.SharedInfo, ""));


        //设置照片选择
        PhotoHelper = new TakePhotoHelper();

        image1Btn = (ImageView) view.findViewById(R.id.image1Btn);
        image1 = (ImageView) view.findViewById(R.id.image1);

        image1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIndex = 1;
                List<People> list = new ArrayList<>();
                list.add(new People(2, "拍照"));
                new SuperDialog.Builder(act)
                        //.setAlpha(0.5f)
                        //.setGravity(Gravity.CENTER)
                        .setTitle("上传照片", ColorRes.negativeButton)
                        .setCanceledOnTouchOutside(false)
                        .setItems(list, new SuperDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                PhotoHelper.init(getTakePhoto(), 2, false, 1, 0, 0);

                            }
                        })
                        .setNegativeButton("取消", null)
                        .setWindowAnimations(R.style.dialogWindowAnim)
                        .build();
            }
        });

        image2Btn = (ImageView) view.findViewById(R.id.image2Btn);
        image2 = (ImageView) view.findViewById(R.id.image2);

        image2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIndex = 2;
                List<People> list = new ArrayList<>();
                list.add(new People(2, "拍照"));
                new SuperDialog.Builder(act)
                        //.setAlpha(0.5f)
                        //.setGravity(Gravity.CENTER)
                        .setTitle("上传照片", ColorRes.negativeButton)
                        .setCanceledOnTouchOutside(false)
                        .setItems(list, new SuperDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                PhotoHelper.init(getTakePhoto(), 2, false, 1, 1000, 1000);

                            }
                        })
                        .setNegativeButton("取消", null)
                        .setWindowAnimations(R.style.dialogWindowAnim)
                        .build();
            }
        });

        edit_auditor = (EditText) view.findViewById(R.id.edit_auditor);
        edit_auditor.setText(dataPref.getString(define.SharedUser, ""));

        submit = (BootstrapButton) view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dataEditor.putString(define.SharedInfo, edit_info.getText().toString());
                dataEditor.putString(define.SharedAuditor, edit_auditor.getText().toString());

                dataEditor.commit();


                if (image1Url == null) {
                    final MaterialStyledDialog.Builder dialog = new MaterialStyledDialog.Builder(act)
                            .setHeaderDrawable(R.drawable.header)
                            .withIconAnimation(false)
                            .setIcon(new IconicsDrawable(act).icon(MaterialDesignIconic.Icon.gmi_comment_alt).color(Color.WHITE))
                            .setTitle("请上传照片A")
                            .setDescription("  ")
                            .setHeaderColor(R.color.dialog)
                            .setPositiveText("确定");
                    dialog.show();
                } else if (image2Url == null) {
                    final MaterialStyledDialog.Builder dialog = new MaterialStyledDialog.Builder(act)
                            .setHeaderDrawable(R.drawable.header)
                            .withIconAnimation(false)
                            .setIcon(new IconicsDrawable(act).icon(MaterialDesignIconic.Icon.gmi_comment_alt).color(Color.WHITE))
                            .setTitle("请上传照片B")
                            .setDescription("  ")
                            .setHeaderColor(R.color.dialog)
                            .setPositiveText("确定");
                    dialog.show();
                } else if (edit_info.getText().toString().isEmpty()) {
                    final MaterialStyledDialog.Builder dialog = new MaterialStyledDialog.Builder(act)
                            .setHeaderDrawable(R.drawable.header)
                            .withIconAnimation(false)
                            .setIcon(new IconicsDrawable(act).icon(MaterialDesignIconic.Icon.gmi_comment_alt).color(Color.WHITE))
                            .setTitle("请输入情况说明")
                            .setDescription("  ")
                            .setHeaderColor(R.color.dialog)
                            .setPositiveText("确定");
                    dialog.show();
                } else {

                    RequestParams params = new RequestParams(define.SubmitCheck);
                    Bundle bundle = act.getIntent().getExtras();
                    if (bundle != null) {
                        if (bundle.getString("id") != null) {
                            params.addQueryStringParameter("id", bundle.getString("id"));
                        }
                    }
                    params.addQueryStringParameter("image1", image1Url);
                    params.addQueryStringParameter("image2", image2Url);
                    params.addQueryStringParameter("info", edit_info.getText().toString());
                    params.addQueryStringParameter("auditor", edit_auditor.getText().toString());

                    x.http().get(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(final String result) {
                            //解析result
                            //重新设置数据
                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final MaterialStyledDialog.Builder dialog = new MaterialStyledDialog.Builder(act)
                                            .setHeaderDrawable(R.drawable.header)
                                            .withIconAnimation(false)
                                            .setIcon(new IconicsDrawable(act).icon(MaterialDesignIconic.Icon.gmi_comment_alt).color(Color.WHITE))
                                            .setTitle(result)
                                            .setDescription("  ")
                                            .setHeaderColor(R.color.dialog)
                                            .setPositiveText("确定")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    act.finish();
                                                }
                                            });
                                    dialog.show();


                                }
                            });

                        }

                        //请求异常后的回调方法
                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                        }

                        //主动调用取消请求的回调方法
                        @Override
                        public void onCancelled(CancelledException cex) {
                        }

                        @Override
                        public void onFinished() {
                        }
                    });

                }
            }
        });


        return view;
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);

        // 拷贝图片,最后通知图库更新
        //复制文件到huayifu目录
        final String fileName = System.currentTimeMillis() + ".jpg";
        final File file = new File(Environment.getExternalStorageDirectory(), "/jindi/" + fileName);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Utils.copyfile(new File(tResult.getImages().get(0).getCompressPath()), file, true);
        act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));

        OSS oss = OSSHelper.getOSSClient(act, define.OSS_KEY, define.OSS_SECRET);
        if (imageIndex == 1) {
            putImage(oss, fileName, file.toString(), image1);
        } else {
            putImage(oss, fileName, file.toString(), image2);
        }

    }

    public void putImage(OSS oss, final String fileName, String filePath, final ImageView im) {
        PutObjectRequest put = new PutObjectRequest(define.bucket, fileName, filePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");

                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                x.image().bind(im,
                        define.endpoint + "/" + fileName,
                        imageOptions);
                if (imageIndex == 1) {
                    image1Url = fileName;
                } else {
                    image2Url = fileName;
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());

                    Toast.makeText(act, "照片上传失败，请检查网络设置", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void takeFail(TResult tResult, String s) {
        super.takeFail(tResult, s);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }
}
