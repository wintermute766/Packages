package ru.sberbank.learning.packages;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
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

        holder.title.setText(packageInfo.packageName);
        holder.icon.setImageResource(R.mipmap.ic_launcher);

        if (packageInfo.applicationInfo != null) {
            LoadInfoTask loadInfoTask = new
                    LoadInfoTask(packageInfo, pm, holder.icon, holder.title);
            loadInfoTask.execute();
        }

        holder.subtitle.setText(packageInfo.packageName);

        return view;
    }

    private static class ViewHolder {
        private ImageView icon;
        private TextView title;
        private TextView subtitle;
    }

    private static class LoadInfoTask extends AsyncTask<Void, Void, ProcessInfo> {

        private PackageInfo packageInfo;
        private PackageManager packageManager;

        private WeakReference<ImageView> imageRef;
        private WeakReference<TextView> textRef;

        public LoadInfoTask(PackageInfo packageInfo, PackageManager packageManager,
                            ImageView imageTarget, TextView textTarget) {
            this.packageInfo = packageInfo;
            this.packageManager = packageManager;

            imageRef = new WeakReference<>(imageTarget);
            textRef = new WeakReference<>(textTarget);
            imageTarget.setTag(packageInfo.packageName);

        }

        @Override
        protected ProcessInfo doInBackground(Void... params) {
            Drawable icon = packageManager
                    .getApplicationIcon(packageInfo.applicationInfo);
            CharSequence label = packageInfo.applicationInfo.loadLabel(packageManager);

            return new ProcessInfo(icon, label);
        }

        @Override
        protected void onPostExecute(ProcessInfo processInfo) {
            ImageView imageView = imageRef.get();
            TextView textView = textRef.get();

            if (imageView == null || textView == null ||
                    !packageInfo.packageName.equals(imageView.getTag().toString())) {
                return;
            }

            imageView.setImageDrawable(processInfo.getIcon());
            textView.setText(processInfo.getTitle());
        }
    }

    static class ProcessInfo {
        private Drawable icon;
        private CharSequence title;

        public ProcessInfo(Drawable icon, CharSequence title) {
            this.icon = icon;
            this.title = title;
        }

        public Drawable getIcon() {
            return icon;
        }

        public CharSequence getTitle() {
            return title;
        }
    }
}
