package com.app.estockcard.view.admin;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.view.admin.home.HomeFragment;
import com.app.estockcard.view.admin.management.ManagementFragment;
import com.app.estockcard.view.admin.order.OrderFragment;
import com.app.estockcard.view.admin.report.ReportFragment;
import com.app.estockcard.view.admin.settings.SettingsFragment;
import com.app.estockcard.view.admin.shift.ShiftFragment;
import com.app.estockcard.view.admin.transaction.TransactionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminMainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener,
        ManagementFragment.OnFragmentInteractionListener, ShiftFragment.OnFragmentInteractionListener,
        ReportFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbarTitle;

    public @BindView(R.id.fab)
    FloatingActionButton fab;

    public @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.nav_view_menu)
    NavigationView navigationView;

    private final String TAG = AdminMainActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private Fragment lastFragmentShow;
    private HomeFragment homeFragment;
    private TransactionFragment transactionFragment;
    private OrderFragment orderFragment;
    private ManagementFragment managementFragment;
    private ReportFragment reportFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.appstr_navigation_drawer_open, R.string.appstr_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View toRet = LayoutInflater.from(getApplicationContext()).inflate(R.layout.admin_nav_header_main, null);
        initComponents();
    }

    private void initComponents() {
        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        transactionFragment = new TransactionFragment();
        orderFragment = new OrderFragment();
        managementFragment = new ManagementFragment();
        reportFragment = new ReportFragment();
        settingsFragment = new SettingsFragment();
        fragmentManager.beginTransaction().add(R.id.content_main, homeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, orderFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, transactionFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, managementFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, reportFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_main, settingsFragment).commit();
    }

    private void setActiveFragment(Fragment fragment) {
        for (Fragment item : fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().hide(item).commitAllowingStateLoss();
        }
        for (Fragment item : fragmentManager.getFragments()) {
            if (fragment.getId() == item.getId()) {
                fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
                lastFragmentShow = fragment;
            }
        }
    }

    private void cancelAllTransaction() {
        if (orderFragment != null) {
            if (orderFragment.isDoTransaction()) {
                orderFragment.cancelAllTransaction();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastFragmentShow != null) {
            setActiveFragment(lastFragmentShow);
        } else {
            setToolbar(getResources().getString(R.string.appstr_create_order));
            setActiveFragment(orderFragment);
            setFloatingBtn(R.drawable.ic_add_shopping_cart, true);
        }
    }

    private void setToolbar(String subtitle) {
        //toolbar.setLogo(navigationIcon);
//        toolbar.setContentInsetStartWithNavigation(0);
//        toolbar.setContentInsetEndWithActions(0);
//        toolbar.setTitleMarginStart(10);
//        toolbar.setTitle(subtitle);
        toolbarTitle.setText(subtitle);
        //toolbar.setSubtitle(subtitle);
        //toolbar.setNavigationIcon(navigationIcon);
    }

    private void setFloatingBtn(int iconID, boolean hide) {
        if (hide) {
            fab.hide();
        } else {
            fab.show();
        }
        if (iconID != -1) {
            fab.setImageResource(iconID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean closeState;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (closeState) {
                super.onBackPressed();
                return;
            }
            closeState = true;
            Toast.makeText(this, R.string.appstr_exit_message, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    closeState = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllTransaction();
    }


    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main, menu);
        this.menu = menu;
//        MenuItem itemCart = menu.findItem(R.id.action_view_transaction);
//        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
//        setBadgeCount(this, icon, "9");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            finish();
        }
//        else if (id == R.id.action_view_transaction) {
//            startActivity(new Intent(this,TestConstraintActivity.class));
//        }

        return super.onOptionsItemSelected(item);
    }

    private void showMenuOption(boolean show) {
        menu.findItem(R.id.action_search).setVisible(show);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        showMenuOption(id == R.id.nav_order);
        if (id == R.id.nav_dashboard) {
            setActiveFragment(homeFragment);
            setToolbar(getResources().getString(R.string.appstr_dashboard));
            setFloatingBtn(-1, true);

        } else if (id == R.id.nav_order) {
            setActiveFragment(orderFragment);
            setToolbar(getResources().getString(R.string.appstr_create_order));
            setFloatingBtn(R.drawable.ic_add_shopping_cart, true);
        } else if (id == R.id.nav_transaction) {
            setActiveFragment(transactionFragment);
            setToolbar(getResources().getString(R.string.appstr_transaction));
            setFloatingBtn(-1, true);
        } else if (id == R.id.nav_management) {
            cancelAllTransaction();
            setActiveFragment(managementFragment);
            setToolbar(getResources().getString(R.string.appstr_management));
            setFloatingBtn(-1, true);
        } else if (id == R.id.nav_report) {
            setActiveFragment(reportFragment);
            setToolbar(getResources().getString(R.string.appstr_report));
            setFloatingBtn(-1, true);
        } else if (id == R.id.nav_settings) {
            setActiveFragment(settingsFragment);
            setToolbar(getResources().getString(R.string.appstr_settings));
            setFloatingBtn(-1, true);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onHomeFragmentInteraction() {

    }

    @Override
    public void onOrderFragmentInteraction(int orderedItemCount) {

    }

    @Override
    public void onManagementFragmentInteraction() {

    }

    @Override
    public void onShiftFragmentInteraction() {

    }

    @Override
    public void onReportFragmentInteraction() {

    }

    @Override
    public void onSettingsFragmentInteraction() {

    }
}
