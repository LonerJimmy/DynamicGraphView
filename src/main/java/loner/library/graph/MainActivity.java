package loner.library.graph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DynamicGraphView dynamicGraphView = (DynamicGraphView) findViewById(R.id.view);
        List<NewReportInfo> list = new ArrayList<>();
        list.add(new NewReportInfo(6, 420, 2, 132));
        list.add(new NewReportInfo(7, 300, 2, 0));
        list.add(new NewReportInfo(8, 450, 2, 68));
        list.add(new NewReportInfo(9, 400, 2, 100));
        dynamicGraphView.setPoint(list);

        dynamicGraphView.start();
    }
}
