package com.g09.levels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Button;
import com.g09.R;

public class BatteryReceiver extends BroadcastReceiver {
    Button btn1, btn2, btn3, btn4;

    @Override
    public void onReceive(Context context, Intent intent) {
        btn1 = ((Lvl7)context).findViewById(R.id.button1);
        btn2 = ((Lvl7)context).findViewById(R.id.button2);
        btn3 = ((Lvl7)context).findViewById(R.id.button3);
        btn4 = ((Lvl7)context).findViewById(R.id.button4);

        BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        long batteryCapacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        if (batteryCapacity == Long.MIN_VALUE) batteryCapacity = 3;

        btn1.setText(Long.toString(batteryCapacity-200));
        btn2.setText(Long.toString(batteryCapacity));
        btn3.setText(Long.toString(batteryCapacity+200));
        btn4.setText(Long.toString(batteryCapacity+400));

        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)){

        }

    }

}
