package desenvolvimento.app.mobileappteste;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class CameraActivity extends Activity {

    private WebView webView;

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

        // Configurar o agente do usuário para um dispositivo móvel
        String userAgent = "Mozilla/5.0 (Linux; Android 10; SM-G975F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.210 Mobile Safari/537.36";
        webSettings.setUserAgentString(userAgent);

        // Carregar uma URL específica
        webView.loadUrl("https://sketchfab.com/models/faf4babdcda04c3aa95192736503802e/embed?autostart=1&cardboard=1&internal=1&tracking=0&ui_infos=0&ui_snapshots=1&ui_stop=0&ui_theatre=1&ui_watermark=0");

        // Configurar o WebView como conteúdo da atividade
        setContentView(webView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
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
}
