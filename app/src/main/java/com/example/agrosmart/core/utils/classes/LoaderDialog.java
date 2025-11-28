package com.example.agrosmart.core.utils.classes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.agrosmart.R;

public class LoaderDialog extends Dialog {
    public LoaderDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Quitamos el título por defecto del dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 2. Asignamos nuestro layout
        setContentView(R.layout.dialog_loader);

        // 3. CRÍTICO: Hacemos el fondo de la ventana del dialog transparente.
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // 4. Evitamos que el usuario lo cierre tocando fuera o con el botón atrás
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
