package com.inflps.speakerCleaner;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

/** @noinspection ALL */
public class WaterEjector {

	public int numSamples;

	private void run() {
		try {
			prepareAudio();
			audioTrack.play();
			playRawSequence(
					generateWaterSignal((int) ((WATER_DURATION_MS / 1000.0) * SAMPLE_RATE)),
					0.0f, 0.5f
			);

			if (isRunning.get()) Thread.sleep(200);
			if (isRunning.get()) {
				playRawSequence(
						generateDustSignal((int) ((DUST_DURATION_MS / 1000.0) * SAMPLE_RATE)),
						0.5f, 1.0f
				);
			}

		} catch (InterruptedException ignored) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	public interface EjectionListener {
		void onStateChanged(boolean isRunning);
		void onProgress(float percent);
	}
	
	private static final int SAMPLE_RATE = 44100;
	private static final int WATER_DURATION_MS = 30000;
	private static final int DUST_DURATION_MS = 30000;
	private static final int WATER_START_FREQ = 150;
	private static final int WATER_END_FREQ = 600;
	private static final int DUST_TONE_FREQ = 85;
	private static final double PULSE_RATE_HZ = 4.0;
	private static final long VIBE_INTERVAL_MS = (long) (1000 / PULSE_RATE_HZ);
	
	private volatile int manualFreq = 150;
	private volatile boolean isPulsing = false;
	private volatile boolean isVibrating = false;
	
	private long lastActionTime = 0;
	private static final long COOLDOWN_MS = 500; 
	
	private AudioTrack audioTrack;
	private Thread workerThread;
	private final AtomicBoolean isRunning = new AtomicBoolean(false);
	private EjectionListener listener;
	private final Vibrator vibrator;
	
	public WaterEjector(Context context) {
		if (context != null) {
			vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		} else {
			vibrator = null;
		}
	}
	
	public void setListener(EjectionListener listener) {
		this.listener = listener;
	}
	
	public synchronized void toggleManual() {
		if (isSpamming()) return;
		
		if (isRunning.get()) {
			stop();
		} else {
			startManualThread();
		}
	}
	
	public synchronized void toggleAuto() {
		if (isSpamming()) return;
		
		if (isRunning.get()) {
			stop();
		} else {
			startAutoThread();
		}
	}
	
	private boolean isSpamming() {
		long now = System.currentTimeMillis();
		if (now - lastActionTime < COOLDOWN_MS) {
			return true;
		}
		lastActionTime = now;
		return false;
	}
	
	private void startManualThread() {
		isRunning.set(true);
		notifyState(true);
		
		workerThread = new Thread(() -> {
			try {
				prepareAudio();
				audioTrack.play();
				
				double phase = 0;
				double pulsePhase = 0;
				double currentFreq = manualFreq; 
				double currentVolume = 0.0; 
				
				int bufferSize = 1024; 
				short[] buffer = new short[bufferSize];
				long lastVibrateCheck = System.currentTimeMillis();
				while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
					double targetFreq;
                    targetFreq = manualFreq;
                    boolean pulseEnabled = isPulsing;
					boolean vibrateEnabled = isVibrating;
					for (int i = 0; i < bufferSize; i++) {
						currentFreq += (targetFreq - currentFreq) * 0.001;
						phase += 2.0 * Math.PI * currentFreq / SAMPLE_RATE;
						if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI;
						double signal = Math.sin(phase);
						double targetVolume = 1.0;
						if (pulseEnabled) {
							pulsePhase += 2.0 * Math.PI * PULSE_RATE_HZ / SAMPLE_RATE;
							if (pulsePhase > 2.0 * Math.PI) pulsePhase -= 2.0 * Math.PI;
							double lfo = (Math.sin(pulsePhase) + 1.0) / 2.0;
							targetVolume = lfo * lfo; // Square for cleaner definition
						}
						currentVolume += (targetVolume - currentVolume) * 0.005;
						buffer[i] = (short) (signal * currentVolume * Short.MAX_VALUE);
					}
					
					audioTrack.write(buffer, 0, bufferSize);
					if (vibrateEnabled) {
						long now = System.currentTimeMillis();
						if ((now - lastVibrateCheck >= VIBE_INTERVAL_MS - 20) && currentVolume > 0.8) {
							triggerVibration(60);
							lastVibrateCheck = now;
						}
					}
				}
				
			} catch (Exception e) {
				Log.e("WaterEjector", "Manual error", e);
			} finally {
				cleanup();
			}
		});
		workerThread.start();
	}
	
	public void setManualFrequency(int hz) {
		if (hz < 100) hz = 100;
		if (hz > 1000) hz = 1000;
		this.manualFreq = hz;
	}
	
	public void setManualPulse(boolean enabled) {
		this.isPulsing = enabled;
	}
	
	public void setVibration(boolean enabled) {
		this.isVibrating = enabled;
		if (!enabled && vibrator != null) {
			vibrator.cancel();
		}
	}

	private void triggerVibration(long ms) {
        if (vibrator != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(ms);
                }
            } catch (Exception ignored) {}
        }
    }
	
	private void startAutoThread() {
		isRunning.set(true);
		notifyState(true);
		
		workerThread = new Thread(this::run);
		workerThread.start();
	}
	
	private void playRawSequence(@NonNull short[] audioData, float startProgress, float endProgress) {
		int blockSize = SAMPLE_RATE / 4;
		int written = 0;
		
		while (written < audioData.length && isRunning.get()) {
			if (Thread.currentThread().isInterrupted()) break;
			
			int remaining = audioData.length - written;
			int toWrite = Math.min(blockSize, remaining);
			audioTrack.write(audioData, written, toWrite);
			written += toWrite;
			
			float segmentProgress = (float) written / audioData.length;
			float globalProgress = startProgress + (segmentProgress * (endProgress - startProgress));
			
			if (listener != null) listener.onProgress(globalProgress);
		}
	}
	
	@NonNull
	private short[] generateWaterSignal(int numSamples) {
		short[] samples = new short[numSamples];
		double phase = 0;
		double sweepSpeed = 4.0;
		for (int i = 0; i < numSamples; i++) {
			double t = (double) i / SAMPLE_RATE;
			double freqOffset = (Math.sin(2 * Math.PI * (sweepSpeed / 10.0) * t) + 1) / 2; 
			double currentFreq = WATER_START_FREQ + (freqOffset * (WATER_END_FREQ - WATER_START_FREQ));
			phase += 2.0 * Math.PI * currentFreq / SAMPLE_RATE;
			double pulse = 0.8 + 0.2 * Math.sin(2 * Math.PI * 2.0 * t); 
			samples[i] = (short) (Math.sin(phase) * Short.MAX_VALUE * pulse);
		}
		return samples;
	}

	@NonNull
	private short[] generateDustSignal(int numSamples) {
        this.numSamples = numSamples;
        short[] samples = new short[numSamples];
		int cursor = 0;
		double startBeats = 2.0; double endBeats = 15.0;  
		while (cursor < numSamples) {
			double progress = (double) cursor / numSamples;
			double curBeats = startBeats + (progress * (endBeats - startBeats));
			int cycle = (int) (SAMPLE_RATE / curBeats);
			int on = (int) (cycle * 0.6); int off = cycle - on;
			double phase = 0; 
			for (int i = 0; i < on && cursor < numSamples; i++) {
				phase += 2.0 * Math.PI * DUST_TONE_FREQ / SAMPLE_RATE;
				double val = Math.sin(phase);
				if (val > 0.1) val = 1.0; else if (val < -0.1) val = -1.0;
				double env = 1.0; int f = 100;
				if (i < f) env = (double) i / f; else if (i > on - f) env = (double) (on - i) / f;
				samples[cursor++] = (short) (val * Short.MAX_VALUE * 0.9 * env);
			}
			for (int i = 0; i < off && cursor < numSamples; i++) samples[cursor++] = 0;
		}
		return samples;
	}
	
	public void stop() {
		if (!isRunning.get()) return;
		isRunning.set(false);
		if (workerThread != null) {
			workerThread.interrupt();
		}
		if (vibrator != null) vibrator.cancel();
	}
	
	public boolean isRunning() {
		return isRunning.get();
	}
	
	private void prepareAudio() {
		if (audioTrack != null) audioTrack.release();
		int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioTrack = new AudioTrack.Builder()
            .setAudioAttributes(new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build())
            .setAudioFormat(new AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(SAMPLE_RATE)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build())
            .setBufferSizeInBytes(bufferSize * 2)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build();
        }
        audioTrack.setVolume(1.0f);
	}
	
	private void cleanup() {
		isRunning.set(false);
		if (audioTrack != null) {
			try {
				audioTrack.pause(); 
				audioTrack.flush(); 
				audioTrack.stop();
				audioTrack.release();
			} catch (Exception ignored) {}
			audioTrack = null;
		}
		
		if (vibrator != null) {
			try { vibrator.cancel(); } catch(Exception ignored){}
		}
		notifyState(false);
	}
	
	private void notifyState(boolean running) {
		if (listener != null) {
			listener.onStateChanged(running);
		}
	}
}
