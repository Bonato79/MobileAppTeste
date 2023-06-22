package desenvolvimento.app.mobileappteste;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {
    private WebView webView1;
    private WebView webView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        webView1 = findViewById(R.id.webview1);
        webView2 = findViewById(R.id.webview2);

        // Carrega uma URL na primeira WebView
        webView1.loadUrl("https://sketchfab.com/3d-models/totem-oral-b-faf4babdcda04c3aa95192736503802e");

        // Configura um WebViewClient para a primeira WebView
        webView1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Obtém o título e a URL da primeira WebView
                webView1.evaluateJavascript(
                        "(function() { return {title: document.title, url: document.URL}; })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // Insere as informações na segunda WebView
                                String js = "javascript:document.open();document.write('<html><body>' + " + value + ".title + '<br><br>' + " + value + ".url + '</body></html>');document.close();";
                                webView2.loadUrl(js);
                            }
                        });
            }
        });
    }
}
