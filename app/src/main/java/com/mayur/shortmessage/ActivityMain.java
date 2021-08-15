package com.mayur.shortmessage;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.mayur.R;
import com.mayur.shortmessage.data.Constant;
import com.mayur.shortmessage.data.DatabaseHandler;
import com.mayur.shortmessage.data.model.Message;
import com.mayur.shortmessage.data.store.ContactStore;
import com.mayur.shortmessage.data.store.MessageStore;
import com.mayur.shortmessage.fragment.ContactFragment;
import com.mayur.shortmessage.fragment.FragmentAdapter;
import com.mayur.shortmessage.fragment.MessageFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {



    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public static MessageFragment f_message;
    private ContactFragment f_contact;
    public FloatingActionButton fab;

    private Toolbar searchToolbar;
    private boolean isSearch = false;
    private ViewPager viewPager;
    private DatabaseHandler db;

    public ContactStore contacsStore;
    public MessageStore messageStore;
    public static List<Message> messageList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_messaging);
        setupDrawerLayout();

        db  = new DatabaseHandler(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar_viewpager);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_search);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityNewMessage.class);
                startActivity(i);
            }
        });

        //initToolbar();
        prepareActionBar(toolbar);

        contacsStore = new ContactStore(getApplicationContext());
        messageStore = new MessageStore(ActivityMain.this);
        messageList = messageStore.getAllconversation();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                closeSearch();
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        // for system bar in lollipop
        Window window = this.getWindow();

        if (Constant.getAPIVerison() >= 5.0) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        if (f_message == null) {
            f_message = new MessageFragment();
        }

        if (f_contact == null) {
            f_contact = new ContactFragment();
        }

        adapter.addFragment(f_message, "MESSAGE");
        adapter.addFragment(f_contact, "CONTACT");

        viewPager.setAdapter(adapter);
    }

    private void prepareActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(isSearch ? android.R.color.darker_gray : R.color.colorPrimary));
        }
        if (!isSearch) {
            settingDrawer();
        }
    }

    private void settingDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                actionDrawerMenu(menuItem.getItemId());
                return true;
            }
        });
    }

    private void actionDrawerMenu(int itemId) {
        switch (itemId) {
            case R.id.nav_notif:
                Intent i = new Intent(getApplicationContext(), ActivityNotificationSetting.class);
                startActivity(i);
                break;
            case R.id.nav_rate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
            case R.id.nav_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About");
                builder.setMessage(getString(R.string.about_text));
                builder.setNeutralButton("OK", null);
                builder.show();
                break;
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isSearch) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(isSearch ? R.menu.menu_search_toolbar : R.menu.menu_main, menu);
        if (isSearch) {
            //Toast.makeText(getApplicationContext(), "Search " + isSearch, Toast.LENGTH_SHORT).show();
            final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setIconified(false);
            if (viewPager.getCurrentItem() == 0) {
                search.setQueryHint("Search message...");
            } else {
                search.setQueryHint("Search contact...");
            }
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (viewPager.getCurrentItem() == 0) {
                        f_message.mAdapter.getFilter().filter(s);
                    } else {
                        f_contact.mAdapter.getFilter().filter(s);
                    }
                    return true;
                }
            });
            search.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    closeSearch();
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: {
                isSearch = true;
                searchToolbar.setVisibility(View.VISIBLE);
                prepareActionBar(searchToolbar);
                supportInvalidateOptionsMenu();
                return true;
            }
            case android.R.id.home:
                closeSearch();
                return true;
            case R.id.action_refresh: {
                if(viewPager.getCurrentItem()==0){
                    new RefreshMessage().execute("");
                }else{
                    new RefreshContact().execute("");
                }
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeSearch() {
        if (isSearch) {
            isSearch = false;
            if (viewPager.getCurrentItem() == 0) {
                f_message.mAdapter.getFilter().filter("");
            } else {
                f_contact.mAdapter.getFilter().filter("");
            }
            prepareActionBar(toolbar);
            searchToolbar.setVisibility(View.GONE);
            supportInvalidateOptionsMenu();
        }
    }



    private long exitTime = 0;
    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


    @Override
    public void onResume(){
        try{
            Uri uri = MessageStore.URI;
            changeObserver = new ChangeObserver();
            this.getContentResolver().registerContentObserver(uri, true, changeObserver);
        }catch (Exception e){

        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        try{
            f_message.bindView();
        }catch (Exception e){

        }
        super.onRestart();
    }

    @Override
    public void onPause(){
        this.getContentResolver().unregisterContentObserver(changeObserver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private ChangeObserver changeObserver;
    // wil update only when there a change
    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            try{
                if(!loadRunning) {
                    loadRunning = true;
                    changeLoad = new ChangeLoad();
                    changeLoad.execute("");
                }
            }catch (Exception e){

            }

        }
    }

    private ChangeLoad changeLoad;
    private boolean loadRunning = false;

    private class ChangeLoad extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            messageStore.update();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            loadRunning = false;
            messageList = messageStore.getAllconversation();
            f_message.bindView();
            super.onPostExecute(s);
        }
    }

    private class RefreshMessage extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            f_message.onRefreshLoading();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(100);
                messageStore = new MessageStore(ActivityMain.this);
                messageList = messageStore.getAllconversation();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            f_message.onStopRefreshLoading();
            super.onPostExecute(s);
        }
    }

    private class RefreshContact extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            f_contact.onRefreshLoading();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(10);
                db.truncateDB();
                contacsStore = new ContactStore(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            f_contact.onStopRefreshLoading();
            super.onPostExecute(s);
        }
    }


}
