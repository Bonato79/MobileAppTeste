package desenvolvimento.app.mobileappteste;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class CameraActivity extends Activity implements SensorEventListener {

    private WebView webView;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private static final int THRESHOLD = 5; // Limiar de movimento para ativar a simulação de teclas
    private boolean isMovingForward, isMovingBackward, isMovingLeft, isMovingRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar a atividade como tela cheia
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Configurar a atividade como sobrepondo outros aplicativos
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Criar o WebView
        webView = new WebView(this);
        webView.setWebChromeClient(new WebChromeClient());

        // Configurar as configurações do WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Ativar suporte a JavaScript, se necessário
        webSettings.setDomStorageEnabled(true); // Ativar armazenamento DOM, se necessário

        // Carregar uma URL específica
        webView.loadUrl("https://sketchfab.com/models/faf4babdcda04c3aa95192736503802e/embed?autostart=1&cardboard=1&internal=1&tracking=0&ui_infos=0&ui_snapshots=1&ui_stop=0&ui_theatre=1&ui_watermark=0");

        // Configurar o WebView como conteúdo da atividade
        setContentView(webView);

        // Inicializar o sensor de acelerômetro
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();

        // Registrar o listener do sensor de acelerômetro
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();

        // Remover o listener do sensor de acelerômetro
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remover o WebView do layout
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Detectar movimento para frente e trás
        if (Math.abs(x) > THRESHOLD) {
            if (x > 0) {
                if (!isMovingForward) {
                    simulateKeyPress(KeyEvent.KEYCODE_DPAD_UP, true);
                    isMovingForward = true;
                }
            } else {
                if (!isMovingBackward) {
                    simulateKeyPress(KeyEvent.KEYCODE_DPAD_DOWN, true);
                    isMovingBackward = true;
                }
            }
        } else {
            if (isMovingForward) {
                simulateKeyPress(KeyEvent.KEYCODE_DPAD_UP, false);
                isMovingForward = false;
            }
            if (isMovingBackward) {
                simulateKeyPress(KeyEvent.KEYCODE_DPAD_DOWN, false);
                isMovingBackward = false;
            }
        }

        // Detectar movimento para esquerda e direita
        if (Math.abs(y) > THRESHOLD) {
            if (y > 0) {
                if (!isMovingRight) {
                    simulateKeyPress(KeyEvent.KEYCODE_DPAD_RIGHT, true);
                    isMovingRight = true;
                }
            } else {
                if (!isMovingLeft) {
                    simulateKeyPress(KeyEvent.KEYCODE_DPAD_LEFT, true);
                    isMovingLeft = true;
                }
            }
        } else {
            if (isMovingRight) {
                simulateKeyPress(KeyEvent.KEYCODE_DPAD_RIGHT, false);
                isMovingRight = false;
            }
            if (isMovingLeft) {
                simulateKeyPress(KeyEvent.KEYCODE_DPAD_LEFT, false);
                isMovingLeft = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Não é necessário implementar esse método para este caso
    }

    private void simulateKeyPress(int keyCode, boolean isKeyDown) {
        int action = isKeyDown ? KeyEvent.ACTION_DOWN : KeyEvent.ACTION_UP;
        long now = System.currentTimeMillis();
        KeyEvent keyEvent = new KeyEvent(now, now, action, keyCode, 0);
        dispatchKeyEvent(keyEvent);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        // Simular o pressionamento de tecla no WebView
        webView.dispatchKeyEvent(keyEvent);
        return false;
    }
}
