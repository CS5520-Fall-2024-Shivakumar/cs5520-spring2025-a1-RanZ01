package com.example.numad25sp_ranzhou;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends AppCompatActivity {

    private TextView calcDisplay;
    private StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CALC), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calcDisplay = findViewById(R.id.display);

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnPlus, R.id.btnMinus, R.id.btnEquals, R.id.btnClear
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> handleButtonClick(button.getText().toString()));
        }

        adjustTextSizes();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustTextSizes();
    }

    private void adjustTextSizes() {
        int buttonTextSize;
        int displayTextSize;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonTextSize = 8;
            displayTextSize = 16;
        } else {
            buttonTextSize = 32;
            displayTextSize = 48;
        }

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnPlus, R.id.btnMinus, R.id.btnEquals, R.id.btnClear
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonTextSize);
        }

        calcDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, displayTextSize);
    }

    private void handleButtonClick(String text) {
        if (text.equals("x")) {
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
            }
        } else if (text.equals("=")) {
            try {
                String result = evaluateExpression(expression.toString());
                expression = new StringBuilder(result);
            } catch (Exception e) {
                expression = new StringBuilder("Error");
            }
        } else {
            expression.append(text);
        }
        calcDisplay.setText(expression.length() == 0 ? "CALC" : expression.toString());
    }

    private String evaluateExpression(String expr) {
        try {
            if (expr.contains("+")) {
                String[] tokens = expr.split("\\+");
                if (tokens.length == 2) {
                    return String.valueOf(Integer.parseInt(tokens[0]) + Integer.parseInt(tokens[1]));
                }
            } else if (expr.contains("-")) {
                String[] tokens = expr.split("-");
                if (tokens.length == 2) {
                    return String.valueOf(Integer.parseInt(tokens[0]) - Integer.parseInt(tokens[1]));
                }
            }
            return expr;
        } catch (Exception e) {
            return "Error";
        }
    }
}
