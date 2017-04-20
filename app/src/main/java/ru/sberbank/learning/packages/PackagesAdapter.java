package ru.sberbank.learning.packages;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by user10 on 20.04.2017.
 */

public class PackagesAdapter extends BaseAdapter {

    private List<PackageInfo> packages;

    public PackagesAdapter(List<PackageInfo> packages) {
        this.packages = Collections.unmodifiableList(packages);
    }

    @Override
    public int getCount() {
        return packages.size();
    }

    @Override
    public PackageInfo getItem(int position) {
        return packages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.package_list_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.package_icon);
            holder.title = (TextView) view.findViewById(R.id.package_name);
            holder.subtitle = (TextView) view.findViewById(R.id.package_string);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        PackageInfo packageInfo = getItem(position);

        PackageManager pm = parent.getContext().getPackageManager();

        if (packageInfo.applicationInfo != null) {
            CharSequence label = packageInfo.applicationInfo.loadLabel(pm);
            holder.title.setText(label);

            Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
            holder.icon.setImageDrawable(icon);
        } else {
            holder.title.setText(packageInfo.packageName);
            holder.icon.setImageResource(R.mipmap.ic_launcher);
        }

        holder.subtitle.setText(packageInfo.packageName);

        return view;
    }

    private static class ViewHolder {
        private ImageView icon;
        private TextView title;
        private TextView subtitle;

    }
}
