package jetzt.machbarschaft.android.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import jetzt.machbarschaft.android.R;
import jetzt.machbarschaft.android.database.DataAccess;
import jetzt.machbarschaft.android.database.Storage;
import jetzt.machbarschaft.android.database.entitie.Order;
import jetzt.machbarschaft.android.services.ActiveOrderService;
import jetzt.machbarschaft.android.util.OrderUtil;
import jetzt.machbarschaft.android.view.order.OrderStep1AcceptActivity;

public class MedicalWarningsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_medical_warnings);
        loadOrder();
        // Sets the Custom Pager Adapter to display the different slides in the application
        warningsPager = findViewById(R.id.view_pager_warnings);
        TabLayout introSlidesIndicator = findViewById(R.id.first_order_slides_indicator);
        warningsPager.setAdapter(new CustomWarningPagerAdapter(this));
        introSlidesIndicator.setupWithViewPager(warningsPager, true);


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
            DataAccess.getInstance().setOrderStatus(mOrder.getId(), Order.Status.CONFIRMED);
            Storage storage = Storage.getInstance();
            //storage.setCurrentOrder(mOrder);
            Storage.setOrderInProgress(getApplicationContext(), mOrder);
            startOrderNotification();
            storage.setActiveOrder(getApplicationContext(), true);
            Storage.getInstance().setUserNotifyAccepted(getApplicationContext(), mOrder);
            startActivity(new Intent(this, OrderStep1AcceptActivity.class));
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


    private void startOrderNotification() {
        Intent serviceIntent = new Intent(this, ActiveOrderService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
}
