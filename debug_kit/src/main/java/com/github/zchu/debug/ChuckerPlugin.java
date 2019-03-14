package com.github.zchu.debug;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.service.AutoService;
import com.readystatesoftware.chuck.api.Chuck;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class ChuckerPlugin extends Plugin {

    @Nullable
    @Override
    public PluginModule createPluginModule() {
        return new PluginModule() {

            @Override
            public int getName() {
                return R.string.plugin_name_chucker;
            }

            @Override
            public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
                View inflate = layoutInflater.inflate(R.layout.chucker_item_plugin_layout, parent, false);
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.getContext().startActivity(Chuck.getLaunchIntent(v.getContext(), Chuck.SCREEN_HTTP));
                    }
                });
                return inflate;
            }
        };
    }
}
