package com.inflps.speakerCleaner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.Html;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import android.provider.Settings;

import com.inflps.speakerCleaner.WIDGET.FrequencyWaveView;
import com.inflps.speakerCleaner.WIDGET.Slidebar;
import com.inflps.speakerCleaner.WIDGET.SegmentedGaugeView;


/** @noinspection ALL*/
public class MainActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private WaterEjector waterEjector;
	
	private static final int STATE_LEFT = 1;
	private static final int STATE_RIGHT = 2;
	private int currentState = STATE_LEFT;
	private boolean isVibrationEnabled = false;
	private boolean isPulseEnabled = false;
	private String OPTION_STATE_ID = "";
	private double MEDIA_PLAYER_SEED_ID = 0;

    private LinearLayout container1;
	private LinearLayout container2;
	private LinearLayout automode;
	private LinearLayout manualmode;
	private LinearLayout l1;
	private LinearLayout l2;
	private LinearLayout l3;
	private LinearLayout linear30;
	private LinearLayout linear31;
	private ImageView main_phone_illustration;
	private TextView description;
	private LinearLayout linear32;
	private LinearLayout start_btn;
	private LinearLayout linear29;
	private ImageView s_btn_ic;
	private TextView s_btn_text;
	private LinearLayout space_2;
	private LinearLayout info_container;
	private LinearLayout linear25;
	private TextView textview8;
	private SegmentedGaugeView gaugeView;
	private LinearLayout linear26;
	private LinearLayout stop_btn;
	private LinearLayout LinearLayout30;
	private LinearLayout linear24;
	private LinearLayout w1;
	private LinearLayout w2;
	private ImageView imageview1;
	private TextView warn1;
	private ImageView imageview14;
	private TextView warn2;
	private ImageView imageview11;
	private TextView textview9;
	private ImageView imageview6;
	private TextView sc_complete_title;
	private TextView sc_complete_subtitle;
	private LinearLayout linear10;
	private LinearLayout restart_btn;
	private LinearLayout wrk_btn;
	private ImageView restart_btn_ic;
	private TextView restart_btn_txt;
	private ImageView wrk_btn_ic;
	private TextView wrk_btn_txt;
	private LinearLayout linear27;
	private LinearLayout linear28;
	private RelativeLayout frq_value_view;
	private Slidebar frq_adjust_seekbar;
	private LinearLayout t_btn_container;
	private LinearLayout LinearLayout28;
	private LinearLayout toggle_start_stop_frq_btn;
	private LinearLayout LinearLayout27;
	private FrequencyWaveView waveView;
	private LinearLayout hz_container;
	private TextView frq_value;
	private TextView hz_label;
	private LinearLayout toggle_vibration_btn;
	private LinearLayout toggle_pulse_btn;
	private ImageView tv_btn_ic;
	private TextView tv_btn_txt;
	private LinearLayout space_4;
	private ImageView tp_btn_ic;
	private TextView tp_btn_txt;
	private LinearLayout space_3;
	private ImageView tss_btn_ic;
	private LinearLayout toolbar;
	private LinearLayout space;
	private LinearLayout LinearLayout7;
	private ImageView back;
	private LinearLayout linear34;
	private ImageView more;
	private TextView toolbar_title;
	private TextView subtitle;
	private FrameLayout mc;
	private LinearLayout o3;
	private LinearLayout bb_container_2;
	private LinearLayout bb_container_1;
	private LinearLayout selected;
	private LinearLayout unselected;
	private LinearLayout o1;
	private LinearLayout o2;
	private ImageView o1_ic;
	private TextView o1_txt;
	private ImageView o2_ic;
	private TextView o2_txt;
	private ImageView play_msc_btn_ic;
	
	private TimerTask t;
	private MediaPlayer mp;
	private AlertDialog.Builder info;
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
        FrameLayout background = findViewById(R.id.background);
		container1 = findViewById(R.id.container1);
		container2 = findViewById(R.id.container2);
		automode = findViewById(R.id.automode);
		manualmode = findViewById(R.id.manualmode);
		l1 = findViewById(R.id.l1);
		l2 = findViewById(R.id.l2);
		l3 = findViewById(R.id.l3);
		linear30 = findViewById(R.id.linear30);
		linear31 = findViewById(R.id.linear31);
		main_phone_illustration = findViewById(R.id.main_phone_illustration);
		description = findViewById(R.id.description);
		linear32 = findViewById(R.id.linear32);
		start_btn = findViewById(R.id.start_btn);
		linear29 = findViewById(R.id.linear29);
		s_btn_ic = findViewById(R.id.s_btn_ic);
		s_btn_text = findViewById(R.id.s_btn_text);
		space_2 = findViewById(R.id.space_2);
		info_container = findViewById(R.id.info_container);
		linear25 = findViewById(R.id.linear25);
		textview8 = findViewById(R.id.textview8);
		gaugeView = findViewById(R.id.gaugeView);
		linear26 = findViewById(R.id.linear26);
		stop_btn = findViewById(R.id.stop_btn);
		LinearLayout30 = findViewById(R.id.LinearLayout30);
		linear24 = findViewById(R.id.linear24);
		w1 = findViewById(R.id.w1);
		w2 = findViewById(R.id.w2);
		imageview1 = findViewById(R.id.imageview1);
		warn1 = findViewById(R.id.warn1);
		imageview14 = findViewById(R.id.imageview14);
		warn2 = findViewById(R.id.warn2);
		imageview11 = findViewById(R.id.imageview11);
		textview9 = findViewById(R.id.textview9);
		imageview6 = findViewById(R.id.imageview6);
		sc_complete_title = findViewById(R.id.sc_complete_title);
		sc_complete_subtitle = findViewById(R.id.sc_complete_subtitle);
		linear10 = findViewById(R.id.linear10);
		restart_btn = findViewById(R.id.restart_btn);
		wrk_btn = findViewById(R.id.wrk_btn);
		restart_btn_ic = findViewById(R.id.restart_btn_ic);
		restart_btn_txt = findViewById(R.id.restart_btn_txt);
		wrk_btn_ic = findViewById(R.id.wrk_btn_ic);
		wrk_btn_txt = findViewById(R.id.wrk_btn_txt);
		linear27 = findViewById(R.id.linear27);
		linear28 = findViewById(R.id.linear28);
		frq_value_view = findViewById(R.id.frq_value_view);
		frq_adjust_seekbar = findViewById(R.id.frq_adjust_seekbar);
		t_btn_container = findViewById(R.id.t_btn_container);
		LinearLayout28 = findViewById(R.id.LinearLayout28);
		toggle_start_stop_frq_btn = findViewById(R.id.toggle_start_stop_frq_btn);
		LinearLayout27 = findViewById(R.id.LinearLayout27);
		waveView = findViewById(R.id.waveView);
		hz_container = findViewById(R.id.hz_container);
		frq_value = findViewById(R.id.frq_value);
		hz_label = findViewById(R.id.hz_label);
		toggle_vibration_btn = findViewById(R.id.toggle_vibration_btn);
		toggle_pulse_btn = findViewById(R.id.toggle_pulse_btn);
		tv_btn_ic = findViewById(R.id.tv_btn_ic);
		tv_btn_txt = findViewById(R.id.tv_btn_txt);
		space_4 = findViewById(R.id.space_4);
		tp_btn_ic = findViewById(R.id.tp_btn_ic);
		tp_btn_txt = findViewById(R.id.tp_btn_txt);
		space_3 = findViewById(R.id.space_3);
		tss_btn_ic = findViewById(R.id.tss_btn_ic);
		toolbar = findViewById(R.id.toolbar);
		space = findViewById(R.id.space);
		LinearLayout7 = findViewById(R.id.LinearLayout7);
		back = findViewById(R.id.back);
		linear34 = findViewById(R.id.linear34);
		more = findViewById(R.id.more);
		toolbar_title = findViewById(R.id.toolbar_title);
		subtitle = findViewById(R.id.subtitle);
		mc = findViewById(R.id.mc);
		o3 = findViewById(R.id.o3);
		bb_container_2 = findViewById(R.id.bb_container_2);
		bb_container_1 = findViewById(R.id.bb_container_1);
		selected = findViewById(R.id.selected);
		unselected = findViewById(R.id.unselected);
		o1 = findViewById(R.id.o1);
		o2 = findViewById(R.id.o2);
		o1_ic = findViewById(R.id.o1_ic);
		o1_txt = findViewById(R.id.o1_txt);
		o2_ic = findViewById(R.id.o2_ic);
		o2_txt = findViewById(R.id.o2_txt);
		play_msc_btn_ic = findViewById(R.id.play_msc_btn_ic);
		info = new AlertDialog.Builder(this);
		
		start_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (mp != null && mp.isPlaying()) {
					o3.performClick();
					start_btn.performClick();
				}
				l1.setVisibility(View.GONE);
				l2.setVisibility(View.VISIBLE);
				
				// NEW FIXED CODE
				waterEjector.setListener(new WaterEjector.EjectionListener() {
						@Override
						public void onStateChanged(boolean isRunning) {
								runOnUiThread(() -> {
                                    if (!isRunning) {
                                            l2.setVisibility(View.GONE);
                                            l3.setVisibility(View.VISIBLE);
                                    }
                                    // Was previously onStarted()
                                    // Update UI to show "Running" state (e.g., change button text, show progress bar)
                                });
						}
						
						@Override
						public void onProgress(float percent) {
								runOnUiThread(() -> {
										// Update your progress bar or gauge here
										// percent is 0.0 to 1.0
										gaugeView.setProgress(percent);
								});
						}
				});
				
				
				
				waterEjector.toggleAuto(); // Use startAuto() for the main cleaning process
				
			}
		});
		
		stop_btn.setOnClickListener(_view -> {
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);
            if (waterEjector.isRunning()) {
                waterEjector.stop();
            }
        });
		
		restart_btn.setOnClickListener(this::onClick2);
		
		wrk_btn.setOnClickListener(this::onClick);
		
		toggle_start_stop_frq_btn.setOnClickListener(_view -> {
            if (mp != null && mp.isPlaying()) {
                o3.performClick();
                toggle_start_stop_frq_btn.performClick();
            }
            if (waterEjector.isRunning()) {
                waterEjector.stop();
                waveView.setPlaying(false);
                tss_btn_ic.setImageResource(R.drawable.play);
                setBtnBg(toggle_start_stop_frq_btn, "#43292929", "#FF292929", 40);
            }
            else {
                waterEjector.toggleManual();
                waveView.setPlaying(true);
                tss_btn_ic.setImageResource(R.drawable.stop);
                setBtnBg(toggle_start_stop_frq_btn, "#FF8286B5", "#FF8286B5", 40);
            }
        });
		
		toggle_vibration_btn.setOnClickListener(_view -> {
            if (isVibrationEnabled) {
                isVibrationEnabled = false;
                waterEjector.setVibration(false);
                setBtnBg(toggle_vibration_btn, "#43292929", "#FF292929", 90);
            }
            else {
                isVibrationEnabled = true;
                waterEjector.setVibration(true);
                setBtnBg(toggle_vibration_btn, "#FF8286B5", "#FF8286B5", 90);
            }
        });
		
		toggle_pulse_btn.setOnClickListener(_view -> {
            if (isPulseEnabled) {
                isPulseEnabled = false;
                waterEjector.setManualPulse(false);
                waveView.setPulsing(false);
                setBtnBg(toggle_pulse_btn, "#43292929", "#FF292929", 90);
            }
            else {
                isPulseEnabled = true;
                waterEjector.setManualPulse(true);
                waveView.setPulsing(true);
                setBtnBg(toggle_pulse_btn, "#FF8286B5", "#FF8286B5", 90);
            }
        });
		
		back.setOnClickListener(_view -> onBackPressed());
		
		more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				View moptionspopupView = getLayoutInflater().inflate(R.layout.popupview, null);
				final PopupWindow moptionspopup = new PopupWindow(moptionspopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
				LinearLayout background = moptionspopupView.findViewById(R.id.background);
				LinearLayout o1 = moptionspopupView.findViewById(R.id.o1);
				LinearLayout o2 = moptionspopupView.findViewById(R.id.o2);
				LinearLayout o3 = moptionspopupView.findViewById(R.id.o3);
				LinearLayout o4 = moptionspopupView.findViewById(R.id.o4);
				LinearLayout o5 = moptionspopupView.findViewById(R.id.o5);
				background.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)15, (int)1, 0xFFE0E0E0, 0xFF111111));
				i.setAction(Intent.ACTION_VIEW);
				if (Build.VERSION.SDK_INT < 33) {
					o1.setVisibility(View.GONE);
				}
				o1.setOnClickListener(_view1 -> {
                    try {
                        if (Build.VERSION.SDK_INT >= 33) {
                            Intent intent = new Intent("android.settings.APP_LOCALE_SETTINGS");
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        } else {
                            // Fallback for older versions: Open general Application Details
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Unable to open settings.", Toast.LENGTH_SHORT).show();
                    }
                    moptionspopup.dismiss();
                });
				o2.setOnClickListener(_view2 -> {
                    String DismissBtnText = getString(R.string.common_dismiss);
                    StringBuilder html = new StringBuilder();

                    html.append("<b><big>").append(getString(R.string.app_name)).append("</big></b><br/>");
                    html.append("<small>").append(getString(R.string.version_info)).append(" • InfLps").append("</small><br/><br/>");

                    html.append("<b>").append(getString(R.string.help_title)).append("</b><br/>");
                    html.append("&#8226; ").append(getString(R.string.help_sec_placement_p1)).append("<br/>");
                    html.append("&#8226; ").append(getString(R.string.help_sec_placement_p2)).append("<br/><br/>");

                    html.append("<b>").append(getString(R.string.manual_help_title)).append("</b><br/>");
                    html.append("<i>").append(getString(R.string.manual_help_intro)).append("</i><br/>");
                    html.append("&#8226; <b>").append(getString(R.string.manual_help_freq_title)).append(":</b> ").append(getString(R.string.manual_help_freq_desc)).append("<br/>");
                    html.append("&#8226; <b>").append(getString(R.string.manual_help_pulse_title)).append(":</b> ").append(getString(R.string.manual_help_pulse_desc)).append("<br/><br/>");

                    html.append("<b>Credits</b><br/>");
                    html.append("<small>").append(getString(R.string.attribution_music)).append("</small>");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        info.setMessage(Html.fromHtml(html.toString(), Html.FROM_HTML_MODE_LEGACY));
                    }
                    else {
                        info.setMessage(Html.fromHtml(html.toString()));
                    }
                    info.setPositiveButton(DismissBtnText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {

                        }
                    });
                    _CXYZ_dialog_theme(info);
                    moptionspopup.dismiss();
                });
				o3.setOnClickListener(_view3 -> {
                    i.setData(Uri.parse("https://t.me/inflpschat"));
                    startActivity(i);
                    moptionspopup.dismiss();
                });
				o4.setOnClickListener(_view4 -> {
                    i.setData(Uri.parse("https://t.me/inflps_channel"));
                    startActivity(i);
                    moptionspopup.dismiss();
                });
				o5.setOnClickListener(_view5 -> {
                    i.setData(Uri.parse("https://github.com/InfLps/Speaker-cleaner-android-app"));
                    startActivity(i);
                    moptionspopup.dismiss();
                });
				moptionspopup.setAnimationStyle(android.R.style.Animation_Dialog);
				moptionspopup.showAsDropDown(more, 0, 0);
				moptionspopup.setBackgroundDrawable(new BitmapDrawable());
			}
		});
		
		o3.setOnClickListener(_view -> {
            if (!waterEjector.isRunning()) {
                if (mp != null && mp.isPlaying()) {
                    mp.pause();
                    mp.release();
                    mp = null;
                    play_msc_btn_ic.setImageResource(R.drawable.play);
                }
                else {
                    if (mp != null) {
                        mp.release();
                        mp = null;
                    }
                    MEDIA_PLAYER_SEED_ID = getRandomInt(0, 1);
                    if (MEDIA_PLAYER_SEED_ID == 0) {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.legendary_paganini_caprice_house);
                    }
                    else {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.nightlife_in_hamburg_synth_pop_background);
                    }
                    if (mp != null) {
                        mp.start();
                        play_msc_btn_ic.setImageResource(R.drawable.stop);
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                        play_msc_btn_ic.setImageResource(R.drawable.play);
                                        mediaPlayer.release();
                                        mp = null;
                                }
                        });

                    }
                }
            }
        });
		
		o1.setOnClickListener(_view -> {
            OPTION_STATE_ID = "0";
            automode.setVisibility(View.VISIBLE);
            manualmode.setVisibility(View.GONE);
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);
            l3.setVisibility(View.GONE);
            if (waterEjector.isRunning()) {
                waterEjector.stop();
                waveView.setPlaying(false);
            }
            setSelectionState(STATE_LEFT);
            tss_btn_ic.setImageResource(R.drawable.play);
            setBtnBg(toggle_start_stop_frq_btn, "#43292929", "#FF292929", 40);
        });
		
		o2.setOnClickListener(_view -> {
            OPTION_STATE_ID = "1";
            automode.setVisibility(View.GONE);
            manualmode.setVisibility(View.VISIBLE);
            if (waterEjector.isRunning()) {
                waterEjector.stop();
            }
            setSelectionState(STATE_RIGHT);
        });
	}
	
	private void initializeLogic() {
		
		isVibrationEnabled = false;
		isPulseEnabled = false;
		toolbar.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)90, (int)0, Color.TRANSPARENT, 0x33484848));
		bb_container_1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)90, (int)0, Color.TRANSPARENT, 0x33484848));
		selected.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)90, (int)1, Color.TRANSPARENT, 0x33484848));
		o3.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)90, (int)1, Color.TRANSPARENT, 0x33484848));
		w1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)1, Color.TRANSPARENT, 0x33484848));
		w2.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)10, (int)1, Color.TRANSPARENT, 0x33484848));
		setBtnBg(toggle_vibration_btn, "#43292929", "#FF292929", 90);
		setBtnBg(toggle_pulse_btn, "#43292929", "#FF292929", 90);
		setBtnBg(toggle_start_stop_frq_btn, "#43292929", "#FF292929", 40);
		setBtnBg(start_btn, "#43292929", "#FF292929", 90);
		setBtnBg(stop_btn, "#43292929", "#FF292929", 90);
		setBtnBg(restart_btn, "#43292929", "#FF292929", 90);
		setBtnBg(wrk_btn, "#43292929", "#FF292929", 90);
		gaugeView.clearSegments();
		gaugeView.addSegment(1.0f, 0xFF00E5FF); 
		gaugeView.addSegment(1.0f, 0xFFFF4081);
		gaugeView.setShowText(true);
		
		waterEjector = new WaterEjector(this); // Pass Context for vibration support
		
		frq_adjust_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						int hz = 100 + progress;
						waterEjector.setManualFrequency(hz);
						waveView.setFrequency(hz);
						frq_value.setText(String.valueOf((long)(hz)));
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {}
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
	}
	
	
	public void setBtnBg(View btn, String colFill, String colStroke, float radius) {
			GradientDrawable btnUi = new GradientDrawable();
			float d = getApplicationContext().getResources().getDisplayMetrics().density;
			int fillColor = Color.parseColor(colFill); 
			int strokeColor = Color.parseColor(colStroke);
			btnUi.setColor(fillColor);
			btnUi.setCornerRadius(d * radius); 
			btnUi.setStroke((int)(d * 2), strokeColor);
			ColorStateList rippleColor = ColorStateList.valueOf(strokeColor);
			RippleDrawable btnUiRD = new RippleDrawable(rippleColor, btnUi, null);
			btn.setElevation(d * 13);
			btn.setBackground(btnUiRD);
			btn.setClickable(true);
	}
	
	private void setSelectionState(int targetState) {
		    if (currentState == targetState) {
			        return;
			    }
		
		    final LinearLayout container = findViewById(R.id.bb_container_2);
		    final View selected = findViewById(R.id.selected);
		    final View unselected = findViewById(R.id.unselected);
		    ChangeBounds transition = new ChangeBounds();
		    transition.setDuration(300);
		    transition.setInterpolator(new AccelerateDecelerateInterpolator());
		    TransitionManager.beginDelayedTransition(container, transition);
		
		    container.removeView(selected);
		    container.removeView(unselected);
		
		    if (targetState == STATE_RIGHT) {
			        container.addView(unselected);
			        container.addView(selected);
			    } else {
			        container.addView(selected);
			        container.addView(unselected);
			    }
		    currentState = targetState;
	}
	
	
	@Override
	public void onBackPressed() {
		if (OPTION_STATE_ID.equals("1")) {
			o1.performClick();
		}
		else {
			finish();
		}
	}
	public void _CXYZ_dialog_theme(final AlertDialog.Builder _d) {
        //Code generated by App Designer
        // Stroke by InfLps
        final AlertDialog alert = _d.show();
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		double dp = 15;
		double logicalDensity = screen.density;
		int px = (int) Math.ceil(dp * logicalDensity);
		alert.getWindow().getDecorView().setBackground(new GradientDrawable() {
				public GradientDrawable getIns(int cornerRadius, int bgColor, int strokeWidth, int strokeColor) {
						this.setCornerRadius(cornerRadius);
						this.setColor(bgColor);
						this.setStroke(strokeWidth, strokeColor);
						return this;
				}
		}.getIns(px, Color.parseColor("#111111"), (int) (1 * screen.density), Color.parseColor("#474747")));
		alert.getWindow().getDecorView().setPadding(8,8,8,8);
		alert.show();
		
		alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"));
		alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF0000"));
		alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#FF0000"));
		alert.getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		alert.getWindow().getDecorView().setTranslationY(-20);
		TextView textT = (TextView)alert.getWindow().getDecorView().findViewById(android.R.id.message);
		textT.setTextColor(Color.parseColor("#f9f9f9"));
		
		int titleId = getResources().getIdentifier( "alertTitle", "id", "android" ); 
		
		if (titleId > 0) { 
				TextView dialogTitle = (TextView) alert.getWindow().getDecorView().findViewById(titleId); 
				
				if (dialogTitle != null) {
						dialogTitle.setTextColor(Color.parseColor("#FFFFFF"));
				} 
		}
		textT.setTextIsSelectable(true);
	}

	public static int getRandomInt(int min, int max) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? ThreadLocalRandom.current().nextInt(min, max + 1) : new Random().nextInt((max - min) + 1) + min;
	}

	public void set_timer(Timer _timer) {
        this._timer = _timer;
    }

	private void onClick(View _view) {
		l1.setVisibility(View.VISIBLE);
		l3.setVisibility(View.GONE);
	}

	private void onClick2(View _view) {
		start_btn.performClick();
	}
}
