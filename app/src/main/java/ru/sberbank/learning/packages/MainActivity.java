package ru.sberbank.learning.packages;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends Activity {

    private ListView packagesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packagesListView = (ListView) findViewById(R.id.list_packages);

        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        packagesListView.setAdapter(new PackagesAdapter(packages));
    }
}
