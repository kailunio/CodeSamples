public class MyActivity extends Activity {

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 读取代码文件和模板文件
        String code = getFileContent("MyActivity.java");
        String template = getFileContent("code.html");

        // 生成结果
        String html = template.replace("{{code}}", code);

        // 显示到WebView
        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("utf-8") ;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", null, null);
    }

    private String getFileContent(String file){
        String content = "";
        try {
            // 把数据从文件读入内存
            InputStream is = getResources().getAssets().open(file);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int i = is.read(buffer, 0, buffer.length);
            while(i>0){
                bs.write(buffer, 0, i);
                i = is.read(buffer, 0, buffer.length);
            }

            content = new String(bs.toByteArray(), Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return content;
    }
}