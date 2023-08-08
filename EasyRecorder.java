import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/***
 Created By ChikuAI
 Website : https://chikuaicode.com
 Instagram : https://instagram.com/chikuaicode
 Facebook : https://facebook.com/chikuaifb
 YouTube : https://youtube.com/ChikuAi
 */

public class EasyRecorder {

    public interface ChikuAction {
        void onStart();

        void onRecording(long dur);

        void onStop(String path);

        void onEvent(String message);
    }


    private boolean isRecording = false;
    private boolean haveCustomPath = false;
    private String path;
    private final ChikuAction chikuAction;

    private MediaRecorder mediaRecorder = new MediaRecorder();
    private Activity activity;

    public EasyRecorder(Activity activity, ChikuAction chikuAction) {
        this.chikuAction = chikuAction;
        this.activity = activity;
    }

    public void start() {
        if (isRecording) {
            chikuAction.onEvent("Already Recording");
            return;
        }

        if (!haveCustomPath) {
            generatePath();
        }

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
     
        mediaRecorder.setAudioEncodingBitRate(16*44100); // Enahance Recording Quality
        mediaRecorder.setAudioSamplingRate(44100); // Enahance Recording Quality
     
        mediaRecorder.setOutputFile(path);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            chikuAction.onEvent("Failed to start");
            return;
        }

        chikuAction.onStart();
        chikuAction.onEvent("Recoding Started");
        isRecording = true;
        startTimer();
    }

    public void stop() {

        if (!isRecording) {
            chikuAction.onEvent("Already Stopped");
            return;
        }

        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;

        chikuAction.onStop(path);
        stopTimer();

        chikuAction.onEvent("Recording Stop");
    }

    //    ===================================
    Timer easyTimer;
    TimerTask easyTimertask;
    long easyCounter = 0;

    private void startTimer() {
        if (easyTimer != null) {
            easyTimer.cancel();
            easyTimer = null;
        }
        if (easyTimertask != null) {
            easyTimertask.cancel();
            easyTimertask = null;
        }
        easyCounter = 0;

        easyTimer = new Timer();
        easyTimertask = new TimerTask() {
            @Override
            public void run() {
                easyCounter += 1000;
                activity.runOnUiThread(() -> chikuAction.onRecording(easyCounter));
            }
        };
        easyTimer.scheduleAtFixedRate(easyTimertask, 1000, 1000);
    }

    private void stopTimer() {
        try {
            if (easyTimer != null) {
                easyTimer.cancel();
                easyTimer = null;
            }
        } catch (Exception ignored) {
        }
        try {
            if (easyTimertask != null) {
                easyTimertask.cancel();
                easyTimertask = null;
            }
        } catch (Exception ignored) {
        }
        easyCounter = 0;
    }

    private void generatePath() {
        haveCustomPath = false;
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        File savedDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/EasyRecorder/");
        if (!savedDirectory.exists()) {
            savedDirectory.mkdir();
            savedDirectory.mkdirs();
        }
        path = savedDirectory + "/" + timeStamp + "_recording.aac";
    }

    public void setPath(String full_path, String file_name_with_extension) {

        if (full_path.length() < 10 || file_name_with_extension.length() < 4) {
            chikuAction.onEvent("Check File path or File Name");
            return;
        }
        String extension = full_path.substring(full_path.lastIndexOf(".") + 1);
        if (!extension.isEmpty()) {
            try {
                File file = new File(full_path);
                if (file.exists()) {

                    String genPath;

                    if (full_path.endsWith("/")) {
                        if (file_name_with_extension.startsWith("/")) {
                            genPath = full_path + (file_name_with_extension.substring(1));
                        } else {
                            genPath = full_path + file_name_with_extension;
                        }
                    } else {
                        if (file_name_with_extension.startsWith("/")) {
                            genPath = full_path + file_name_with_extension;
                        } else {
                            genPath = full_path + "/" + file_name_with_extension;
                        }
                    }

                    path = genPath;
                    haveCustomPath = true;

                } else {
                    chikuAction.onEvent("Path doesn't exist");
                }
            } catch (Exception ignored) {
                chikuAction.onEvent("Path doesn't exist");
            }
        } else {
            chikuAction.onEvent("Extension missing in filename");
        }
    }
    public boolean isRecording() {
        return this.isRecording;
    }
}
