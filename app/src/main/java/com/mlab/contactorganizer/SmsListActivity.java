package com.mlab.contactorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SmsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listViewSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        switch (menuItem.getItemId()) {
                            // Respond to the action bar's Up/Home button
                            case R.id.nav_signup:
                                //NavUtils.navigateUpFromSameTask(this);
                                return true;
                        }


                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                       // mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


    listViewSMS = (ListView) findViewById(R.id.listViewSMS);

        ArrayList smsList = fetchSmsList();
        if(smsList!=null)
        {
            final MyListAdapter myListAdapter = new MyListAdapter();
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, smsList);
            listViewSMS.setAdapter(myListAdapter);

            /**
             * hint w dymku
             */
            listViewSMS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int pos,   long id) {
                    /**
                     * hint w dymku
                     * Toast.makeText(getApplicationContext(),
                     "text",
                     Toast.LENGTH_SHORT).show();
                     */

                    navigateItem(adapter.getItem(pos));
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sms_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * lista wyslanych smsow: http://www.technotalkative.com/android-fetch-inbox-sms/
     * @return
     */
    public ArrayList fetchSmsList()
    {
        ArrayList smsList = new ArrayList();

        smsList.add("Sms pierwszy");
        smsList.add("Sms drugi");
        smsList.add("Sms trzeci");
        smsList.add("Sms czwarty");
        smsList.add("Sms piaty");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");
        smsList.add("itp itd...");

        return smsList;

    }

    private void navigateItem(Object item) {
        Intent intent = new Intent(this, SmsItemActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("item", (String) item); //Your id
        intent.putExtras(bundle); //Put your id to your next Intent

        startActivity(intent);

    }

    private String[] arrText =
            new String[]{"Text1","Text2","Text3","Text4"
                    ,"Text5","Text6","Text7","Text8","Text9","Text10"
                    ,"Text11","Text12","Text13","Text14","Text15"
                    ,"Text16","Text17","Text18","Text19","Text20"
                    ,"Text21","Text22","Text23","Text24"};
    private String[] arrTemp;

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(arrText != null && arrText.length != 0){
                return arrText.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arrText[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //ViewHolder holder = null;
            final ViewHolder holder;
            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = SmsListActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item, null);
                holder.timeText = (TextView) convertView.findViewById(R.id.timeText);
                holder.smsText = (TextView) convertView.findViewById(R.id.smsText);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            //holder.timeText.setText(arrText[position]);
            holder.smsText.setText(arrText[position]);

            holder.smsText.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateItem(holder.smsText.getText());
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView timeText;
            TextView smsText;
            int ref;
        }


    }
}
