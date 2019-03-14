package com.github.zchu.debug;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class DoraemonPlugin extends Plugin {

    @Nullable
    @Override
    public PluginModule createPluginModule() {
        return new PluginModule() {
            @Override
            public int getName() {
                return R.string.plugin_name_doraemon;
            }

            @Override
            public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
                final View inflate = layoutInflater.inflate(R.layout.doraemon_item_plugin_layout, parent, false);
                inflate.setSelected(DoraemonKit.isShow());
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DoraemonKit.isShow()) {
                            DoraemonKit.hide();
                            inflate.setSelected(false);
                        } else {
                            DoraemonKit.show();
                            inflate.setSelected(true);

                        }

                    }
                });
                return inflate;
            }
        };
    }
}
