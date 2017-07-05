package example.viewsystem.linksu.com.viewsysystem;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private XHorizontalScrollView container;
    private ListView item1, item2, item3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TestButton testButton = (TestButton) findViewById(R.id.testbutton);
//        testButton.smoothScrollTo(100,50);
//        testButton.animatorScroll();
//        testButton.delaySceoll();

        container = (XHorizontalScrollView) findViewById(R.id.container);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) layoutInflater.inflate(R.layout.content_layout, container, false);
            TextView textView = (TextView) layout.findViewById(R.id.title);
            textView.setText("page:" + (i + 1));
            textView.setTextColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            createList(layout);
            container.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListView listView = (ListView) layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList();
        for (int i = 0; i < 50; i++) {
            datas.add("name:" + i);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.name, datas);
        listView.setAdapter(arrayAdapter);
    }

}
