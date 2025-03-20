package com.example.numad25sp_ranzhou;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class PrimeActivity extends AppCompatActivity {
    private volatile boolean isRunning = false;
    private Thread primeThread;
    private volatile int currentNumber;
    private volatile int lastPrime;
    private boolean isResuming = false;

    private TextView currentNumberText, lastPrimeText;
    private CheckBox pacifierSwitch;
    private Button btnFindPrimes, btnTerminateSearch;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);

        currentNumberText = findViewById(R.id.text_current_number);
        lastPrimeText = findViewById(R.id.text_last_prime);
        pacifierSwitch = findViewById(R.id.switch_pacifier);
        btnFindPrimes = findViewById(R.id.btn_find_primes);
        btnTerminateSearch = findViewById(R.id.btn_terminate_search);

        // Initialize UI
        currentNumberText.setText("");
        lastPrimeText.setText("");

        // Restore state after rotation
        if (savedInstanceState != null) {
            currentNumber = savedInstanceState.getInt("currentNumber", 3);
            lastPrime = savedInstanceState.getInt("lastPrime", 0);
            isRunning = savedInstanceState.getBoolean("isRunning", false);
            pacifierSwitch.setChecked(savedInstanceState.getBoolean("pacifierState", false));

            updateUI();
            if (isRunning) {
                isResuming = true;
                startPrimeSearch(true);
            }
        }

        btnFindPrimes.setOnClickListener(v -> startPrimeSearch(false));
        btnTerminateSearch.setOnClickListener(v -> stopPrimeSearch());

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isRunning) {
                    new AlertDialog.Builder(PrimeActivity.this)
                            .setTitle("Confirm Exit")
                            .setMessage("Search is running. Do you want to stop it?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                stopPrimeSearch();
                                finish();
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    finish();
                }
            }
        });
    }

    private synchronized void startPrimeSearch(boolean isResume) {
        if (isRunning && !isResume) return;

        isRunning = true;
        if (!isResume) {
            currentNumber = 3;
            lastPrime = 0;
        }
        updateUI();

        primeThread = new Thread(() -> {
            int num = currentNumber;
            long lastUpdateTime = 0;
            final long UPDATE_INTERVAL = 100; // 100ms update interval

            while (isRunning) {
                if (isPrime(num)) {
                    synchronized (this) {
                        lastPrime = num;
                    }
                }

                final int checkingNum = num;
                final int latestPrime;
                synchronized (this) {
                    latestPrime = lastPrime;
                    currentNumber = num;
                }

                // Throttle UI updates
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdateTime >= UPDATE_INTERVAL) {
                    uiHandler.post(() -> {
                        if (!isDestroyed()) {
                            currentNumberText.setText("Checking: " + checkingNum);
                            lastPrimeText.setText(latestPrime == 0 ? "" : "Last Prime: " + latestPrime);
                        }
                    });
                    lastUpdateTime = currentTime;
                }

                num += 2;

                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
            }
        });

        primeThread.start();
        isResuming = false;
    }

    private synchronized void stopPrimeSearch() {
        isRunning = false;
        if (primeThread != null) {
            primeThread.interrupt();
            primeThread = null;
        }
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        if (num % 2 == 0 && num != 2) return false;
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) return false;
        }
        return true;
    }

    private void updateUI() {
        currentNumberText.setText(currentNumber > 0 ? "Checking: " + currentNumber : "");
        lastPrimeText.setText(lastPrime > 0 ? "Last Prime: " + lastPrime : "");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentNumber", currentNumber);
        outState.putInt("lastPrime", lastPrime);
        outState.putBoolean("isRunning", isRunning);
        outState.putBoolean("pacifierState", pacifierSwitch.isChecked());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPrimeSearch();
    }
}