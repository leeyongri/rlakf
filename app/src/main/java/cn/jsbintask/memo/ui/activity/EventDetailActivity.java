package cn.jsbintask.memo.ui.activity;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import cn.jsbintask.memo.Constants;
import cn.jsbintask.memo.R;
import cn.jsbintask.memo.base.BaseActivity;
import cn.jsbintask.memo.entity.Event;
import cn.jsbintask.memo.manager.ClockManager;
import cn.jsbintask.memo.manager.EventManager;
import cn.jsbintask.memo.receiver.ClockReceiver;
import cn.jsbintask.memo.service.ClockService;
import cn.jsbintask.memo.util.AlertDialogUtil;
import cn.jsbintask.memo.util.DateTimeUtil;
import cn.jsbintask.memo.util.StringUtil;
import cn.jsbintask.memo.util.ToastUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jsbintask.memo.base.BaseActivity;
import cn.jsbintask.memo.entity.Event;
import cn.jsbintask.memo.manager.ClockManager;
import cn.jsbintask.memo.manager.EventManager;
import cn.jsbintask.memo.receiver.ClockReceiver;
import cn.jsbintask.memo.service.ClockService;
import cn.jsbintask.memo.util.AlertDialogUtil;
import cn.jsbintask.memo.util.DateTimeUtil;
import cn.jsbintask.memo.util.StringUtil;
import cn.jsbintask.memo.util.ToastUtil;



public class EventDetailActivity extends BaseActivity {
    public static final String EXTRA_IS_EDIT_EVENT = "extra.is.edit.event";
    public static final String EXTRA_EVENT_DATA = "extra.event.data";
    public static final String EXTRA_IS_ADD_EVENT = "extra.is.create.event";

    private boolean isEditEvent;
    private boolean isAddEvent;
    private EventManager mEventManager = EventManager.getInstance();
    private ClockManager mClockManager = ClockManager.getInstance();
    @BindView(R.id.ll_update_time)
    LinearLayout llUpdateTime;
    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.tv_remind_time_picker)
    EditText tvRemindTime;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.tv_last_edit_time)
    TextView tvUpdateTime;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.chb_is_important)
    CheckBox chbIsImportant;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void initView() {
        isEditEvent = getIntent().getBooleanExtra(EXTRA_IS_EDIT_EVENT, false);
        isAddEvent = getIntent().getBooleanExtra(EXTRA_IS_ADD_EVENT, false);
        judgeOperate();
    }

    private void judgeOperate() {

        llUpdateTime.setVisibility(isAddEvent ? View.GONE : View.VISIBLE);

        setEditTextReadOnly(edTitle, !isEditEvent && !isAddEvent);

        setEditTextReadOnly(edContent, !isEditEvent && !isAddEvent);

        setEditTextReadOnly(tvRemindTime, true);

        tvRemindTime.setClickable(isEditEvent || isAddEvent);

        tvConfirm.setVisibility(isEditEvent || isAddEvent ? View.VISIBLE : View.GONE);

        ivEdit.setVisibility(!isEditEvent && !isAddEvent ? View.VISIBLE : View.GONE);

        ivDelete.setVisibility(!isAddEvent ? View.VISIBLE : View.GONE);

        chbIsImportant.setClickable(isEditEvent || isAddEvent);
    }

    @Override
    protected void initData() {
        if (!isAddEvent) {
            Event event = getIntent().getParcelableExtra(EXTRA_EVENT_DATA);

            tvUpdateTime.setText(event.getmUpdatedTime());
            edTitle.setText(event.getmTitle());
            edContent.setText(event.getmContent());
            tvRemindTime.setText(event.getmRemindTime());
            chbIsImportant.setChecked(event.getmIsImportant() == Constants.EventFlag.IMPORTANT);
        }
    }

    @OnClick(R.id.iv_back)
    public void backImageClick(View view) {
        finish();
    }

    @OnClick(R.id.iv_delete)
    public void deleteImageClick(View view) {
        if (!isAddEvent) {
            AlertDialogUtil.showDialog(this, R.string.delete_event_msg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Event event = getIntent().getParcelableExtra(EventDetailActivity.EXTRA_EVENT_DATA);
                    if (mEventManager.removeEvent(event.getmId())) {
                        ToastUtil.showToastShort(R.string.delete_successful);
                        mClockManager.cancelAlarm(buildIntent(event.getmId()));
                        mEventManager.flushData();
                        postToMainActivity();
                    } else {
                        ToastUtil.showToastShort(R.string.delete_failed);
                    }
                }
            });
        }
    }


    private void postToMainActivity() {
        Intent intent = new Intent();
        intent.setClass(EventDetailActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_event_detail;
    }


    @OnClick(R.id.tv_remind_time_picker)
    public void datePickClick(View view) {
        if (isEditEvent || isAddEvent) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String time = year + "-" + StringUtil.getLocalMonth(month) + "-" + StringUtil.getMultiNumber(dayOfMonth) + " " + StringUtil.getMultiNumber(hourOfDay) + ":" + StringUtil.getMultiNumber(minute);
                            tvRemindTime.setText(time);
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                    timePickerDialog.show();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            dialog.show();
        }
    }

    @OnClick(R.id.iv_edit)
    public void editImageClick(View view) {
        if (!isEditEvent) {
            ToastUtil.showToastShort(R.string.enter_edit_mode);
            ivEdit.setVisibility(View.GONE);
            isEditEvent = true;
            judgeOperate();
        }
    }

    @OnClick(R.id.tv_confirm)
    public void confirmClick(View view) {

        if (isEditEvent || isAddEvent) {
            Event event = buildEvent();

            if (!mEventManager.checkEventField(event)) {
                return;
            }
            if (mEventManager.saveOrUpdate(event)) {
                if (isEditEvent) {
                    ToastUtil.showToastShort(R.string.update_successful);
                } else if (isAddEvent) {
                    ToastUtil.showToastShort(R.string.create_successful);
                    event.setmId(mEventManager.getLatestEventId());
                }

                mClockManager.addAlarm(buildIntent(event.getmId()), DateTimeUtil.str2Date(event.getmRemindTime()));
                mEventManager.flushData();
                postToMainActivity();
            } else {
                if (isEditEvent) {
                    ToastUtil.showToastShort(R.string.update_failed);
                } else if (isAddEvent) {
                    ToastUtil.showToastShort(R.string.create_failed);
                }
            }
        }
    }

    private PendingIntent buildIntent(int id) {
        Intent intent = new Intent();
        intent.putExtra(ClockReceiver.EXTRA_EVENT_ID, id);
        intent.setClass(this, ClockService.class);

        return PendingIntent.getService(this, 0x001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @OnClick(R.id.scroll_view)
    public void scrollViewClick(View view) {
        if (isAddEvent || isEditEvent) {

            setEditTextReadOnly(edContent, false);
        }
    }

    @NonNull
    private Event buildEvent() {
        Event event = new Event();
        if (isEditEvent) {
            event.setmId(((Event) getIntent().getParcelableExtra(EXTRA_EVENT_DATA)).getmId());
            event.setmCreatedTime(((Event) getIntent().getParcelableExtra(EXTRA_EVENT_DATA)).getmCreatedTime());
        }
        event.setmRemindTime(tvRemindTime.getText().toString());
        event.setmTitle(edTitle.getText().toString());
        event.setmIsImportant(chbIsImportant.isChecked() ? Constants.EventFlag.IMPORTANT : Constants.EventFlag.NORMAL);
        event.setmContent(edContent.getText().toString());
        event.setmUpdatedTime(DateTimeUtil.dateToStr(new Date()));
        return event;
    }

    private void setEditTextReadOnly(EditText editText, boolean readOnly) {
        editText.setFocusable(!readOnly);
        editText.setFocusableInTouchMode(!readOnly);
        editText.setCursorVisible(!readOnly);
        editText.setTextColor(getColor(readOnly ? R.color.gray3 : R.color.black));
    }
}
