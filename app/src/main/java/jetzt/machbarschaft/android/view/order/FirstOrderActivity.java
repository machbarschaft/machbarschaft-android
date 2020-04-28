package jetzt.machbarschaft.android.view.order;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.util.OrderUtil;

public class FirstOrderActivity extends AppCompatActivity {

    private Order mOrder;
    public static final String EXTRA_ORDER_ID = "orderId";

    private ViewPager warningsPager;

    private Boolean checkBoxBool3 = false;
    private Boolean checkBoxBool2 = false;
    private Boolean checkBoxBool1 = false;
    private Boolean checkBoxBool4 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_first);

        // Sets the Custom Pager Adapter to display the different slides in the application
        warningsPager = findViewById(R.id.viewPagerWarnings);
        TabLayout introSlidesIndicator = findViewById(R.id.first_order_slides_indicator);
        warningsPager.setAdapter(new CustomWarningPagerAdapter(this));
        introSlidesIndicator.setupWithViewPager(warningsPager, true);
        loadOrder();


    }


    public void warningsNextPress(View view) {
        if(checkBoxBool4 && checkBoxBool3 && checkBoxBool2 && checkBoxBool1)
        warningsPager.setCurrentItem(warningsPager.getCurrentItem()+1, true);
        else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.medical_warnings_not_checked), Toast.LENGTH_SHORT).show();
    }

    public void checkBox4(View view) {
        checkBoxBool4 = !checkBoxBool4;
    }

    public void checkBox3(View view) {
        checkBoxBool3 = !checkBoxBool3;
    }

    public void checkBox2(View view) {
        checkBoxBool2 = !checkBoxBool2;
    }

    public void checkBox1(View view) {
        checkBoxBool1 = !checkBoxBool1;
    }

    public void startHome(View view) {
        if(checkBoxBool4 && checkBoxBool3 && checkBoxBool2 && checkBoxBool1) {
            Storage.getInstance().setUserNotifyAccepted(getApplicationContext(), mOrder);
            startActivity(new Intent(this, OrderAcceptActivity.class));
            finishAfterTransition();
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.medical_warnings_not_checked), Toast.LENGTH_SHORT).show();
        }
    }


    private void loadOrder() {
        Intent intent = getIntent();
        if (intent != null) {
            final String orderId = intent.getStringExtra(EXTRA_ORDER_ID);

            DataAccess.getInstance().getOrderById(orderId, order -> {
                mOrder = (Order) order;
            });
        }
    }

    public void warningTwoBtnCancel(View view) {
        OrderUtil.cancelOrder(mOrder, getApplicationContext());
    }

    public void warningOneBtnCancel(View view) {
        OrderUtil.cancelOrder(mOrder, getApplicationContext());
    }
}
